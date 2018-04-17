package jvm;

import java.util.EventListener;
import java.util.Set;

/**
 * 
 * @author pengbingting
 *
 */
public interface IVmEventListener extends EventListener{

	public void onEvent(Set<Integer> vmPids);
}
