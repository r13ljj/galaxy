package jvm;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.ZLibUtils;
import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.sink.kafka.KafkaSinkConstants;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class ElogJvmMontiorSource extends AbstractSource implements EventDrivenSource, Configurable {
	private final static Logger logger = LoggerFactory.getLogger(ElogJvmMontiorSource.class);
	
	private final static AtomicInteger count = new AtomicInteger(0);

	private final static Gson GSON = new GsonBuilder().create();
	
	private static ScheduledExecutorService schedulerExecutorService = null ;
	/**
	 * 解析完 source 后，会将解析的key value pair 传入给指定的业务实现接口。
	 * 例如：如果你配置的source 是这样子的：
	 * 
	 * a1.sources.jvm.type = source.ElogJVMMontiorSource
	   a1.sources.jvm.channels = c1
	 * 
	 */
	@Override
	public void configure(Context context) {
		ImmutableMap<String, String> paramMap = context.getParameters();
		paramMap.forEach(new BiConsumer<String, String>() {
			public void accept(String t, String u) {
				System.err.println(t+"="+u);
			};
		});
		
		JvmMonitorManager.init(new JdkToolsJvmMonitorImpl(),new DefaultProcessFilter(),new ClassNameFilter());
	}

	@Override
	public synchronized void start() {
		initSchedulerExecutor();
		schedulerExecutorService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				collectJvmProcessInfo();
			}
		}, 0, 1, TimeUnit.MILLISECONDS);
	}

	private void initSchedulerExecutor(){
		if(schedulerExecutorService != null){
			return ;
		}
		
		schedulerExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, "jvm-schuduler-executor-"+count.incrementAndGet());
				if (thread.isDaemon()){
					thread.setDaemon(false);
				}
				
				if (thread.getPriority() != 5){
					thread.setPriority(5);
				}
				
				thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						logger.error("jvm-monitor-scheduler", e);
						if(t.isInterrupted() && ! Thread.interrupted()){
							t.interrupt();
						}
					}
				});
				
				return thread;
			}
		});
	}

	private void collectJvmProcessInfo(){
		try {
			List<ProcessInfo> processInfos = JvmMonitorManager.getAllProcessInfo();
			try {
				JvmMonitorManager.pendingJstatInfo(processInfos);
				JvmMonitorManager.jmapHisto(processInfos, false);
				JvmMonitorManager.jstackInfo(processInfos);
				
				ChannelProcessor channelProcessor = getChannelProcessor();
				
				for(ProcessInfo processInfo:processInfos){
					SimpleEvent simpleEvent = new SimpleEvent();
					HashMap<String, String> headers = new HashMap<>();
					headers.put(KafkaSinkConstants.TOPIC_HEADER, "jvmMonitorTopic");
					headers.put(KafkaSinkConstants.KEY_HEADER, processInfo.getLocalHost());
					String result = GSON.toJson(processInfo);
					System.err.println("orginal---->"+result.getBytes(Charset.forName("utf-8")).length + " Byte");
					//数据量可能过大，压缩传输,
					byte[] resultB = ZLibUtils.compress(result.getBytes(Charset.forName("utf-8")));
					System.err.println("one-->"+resultB.length + " Byte");
					simpleEvent.setBody(resultB);
					simpleEvent.setHeaders(headers);
					channelProcessor.processEvent(simpleEvent);
				}
			} finally {
				for(ProcessInfo processInfo:processInfos){
					processInfo.helpGC();
				}
				
				processInfos.clear();
				processInfos = null ;
			}
		} catch (Exception e) {
		}
	}
	
	public static class ProcessFilter implements IProcessInfoFilter {

		@Override
		public boolean accept(ProcessInfo processInfo) {

			return !(processInfo.getMain() == null || processInfo.getMain().trim().length() == 0);
		}
	}
	
	public static class ClassNameFilter implements IClassNameFilter{

		@Override
		public boolean accept(String line) {
			//line.contains("com.hqyg") || line.contains("com.globalegrow") || line.contains("globalegrow") || line.contains("hqyg")
			return true;
		}
	}
	
	@Override
	public synchronized void stop() {
		schedulerExecutorService.shutdown();
		schedulerExecutorService = null ;
		super.stop();
	}
}
