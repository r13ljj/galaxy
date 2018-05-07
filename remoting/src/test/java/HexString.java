/**
 * <pre>
 *
 *  File: HexString.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/4/27				lijunjun				Initial.
 *
 * </pre>
 */
public class HexString {

    public static void main(String[] args) {
        System.out.println( "0x"+Long.toHexString(0L));
        System.out.println( "0x"+Long.toHexString(1L));
        System.out.println( "0x"+Long.toHexString(2L));
        System.out.println( "0x"+Long.toHexString(3L));
        System.out.println( "0x"+Integer.toHexString(0));
        System.out.println( "0x"+Integer.toHexString(1));
        System.out.println( "0x"+Integer.toHexString(2));
        System.out.println( "0x"+Integer.toHexString(3));
        String str = "{\n" +
                "    \"index\": {\n" +
                "    \t\"number_of_replicas\":2,\n" +
                "        \"max_result_window\": 100000,\n" +
                "        \"analysis\": {\n" +
                "            \"char_filter\": {\n" +
                "                \"special_char_replace_filter\": {\n" +
                "                    \"type\": \"pattern_replace\",\n" +
                "                    \"pattern\": \"_\",\n" +
                "                    \"replacement\": \" \"\n" +
                "                },\n" +
                "                \"whitespace_replace_filter\": {\n" +
                "                    \"pattern\": \" \",\n" +
                "                    \"type\": \"pattern_replace\",\n" +
                "                    \"replacement\": \"\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"filter\": {\n" +
                "                \"shingle\": {\n" +
                "                    \"type\": \"shingle\",\n" +
                "                    \"min_shingle_size\": 2,\n" +
                "                    \"max_shingle_size\": 4,\n" +
                "                    \"output_unigrams\": true\n" +
                "                },\n" +
                "                \"stemmer_ar\": {\n" +
                "\t\t            \"type\": \"stemmer\",\n" +
                "\t\t            \"language\": \"arabic\"\n" +
                "\t\t        }\n" +
                "            },\n" +
                "\t\t\t\"normalizer\": {\n" +
                "        \t\t\"lowercase_normal\": {\n" +
                "          \t\t\t\"type\": \"custom\",\n" +
                "          \t\t\t\"filter\": [\n" +
                "          \t\t\t\t\"lowercase\"\n" +
                "          \t\t\t]\n" +
                "        \t\t}\n" +
                "\t\t\t},\n" +
                "            \"analyzer\": {\n" +
                "                \"default\": {\n" +
                "                    \"tokenizer\": \"standard\",\n" +
                "                    \"filter\": [\n" +
                "                        \"standard\",\n" +
                "                        \"lowercase\",\n" +
                "                        \"porter_stem\"\n" +
                "                    ],\n" +
                "                    \"char_filter\": [\n" +
                "                        \"special_char_replace_filter\"\n" +
                "                    ]\n" +
                "                },\n" +
                "\t\t\t\t\"default_ar\" : {\n" +
                "\t\t\t\t\t\"filter\" : [\n" +
                "\t\t\t\t\t\t\"standard\",\n" +
                "\t\t\t\t\t\t\"stemmer_ar\"\n" +
                "\t\t\t\t\t],\n" +
                "\t\t\t\t\t\"type\" : \"custom\",\n" +
                "\t\t\t\t\t\"tokenizer\" : \"standard\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"shingle_analyzer_ar\": {\n" +
                "\t\t            \"type\": \"custom\",\n" +
                "\t\t            \"tokenizer\": \"standard\",\n" +
                "\t\t            \"filter\": [\n" +
                "\t\t              \"standard\",\n" +
                "\t\t              \"stemmer_ar\",\n" +
                "\t\t              \"shingle\"\n" +
                "\t\t            ]\n" +
                "\t\t        },\n" +
                "                \"shingle_analyzer\": {\n" +
                "                    \"type\": \"custom\",\n" +
                "                    \"tokenizer\": \"standard\",\n" +
                "                    \"filter\": [\n" +
                "                        \"standard\",\n" +
                "                        \"lowercase\",\n" +
                "                        \"porter_stem\",\n" +
                "                        \"shingle\"\n" +
                "                    ],\n" +
                "                    \"char_filter\": [\n" +
                "                        \"special_char_replace_filter\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"keyword_analyzer\": {\n" +
                "                    \"type\": \"custom\",\n" +
                "                    \"tokenizer\": \"keyword\",\n" +
                "                    \"filter\": [\n" +
                "                        \"lowercase\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"whitespace_analyzer\": {\n" +
                "                    \"type\": \"custom\",\n" +
                "                    \"tokenizer\": \"whitespace\",\n" +
                "                    \"filter\": [\n" +
                "                        \"lowercase\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        System.out.println(str.length());
        System.out.println(str.getBytes().length);
        String str1 = "{\\\"index\\\":{\\\"number_of_replicas\\\":2,\\\"max_result_window\\\":100000,\\\"analysis\\\":{\\\"char_filter\\\":{\\\"special_char_replace_filter\\\":{\\\"type\\\":\\\"pattern_replace\\\",\\\"pattern\\\":\\\"_\\\",\\\"replacement\\\":\\\" \\\"},\\\"whitespace_replace_filter\\\":{\\\"pattern\\\":\\\" \\\",\\\"type\\\":\\\"pattern_replace\\\",\\\"replacement\\\":\\\"\\\"}},\\\"filter\\\":{\\\"shingle\\\":{\\\"type\\\":\\\"shingle\\\",\\\"min_shingle_size\\\":2,\\\"max_shingle_size\\\":4,\\\"output_unigrams\\\":true},\\\"stemmer_ar\\\":{\\\"type\\\":\\\"stemmer\\\",\\\"language\\\":\\\"arabic\\\"}},\\\"normalizer\\\":{\\\"lowercase_normal\\\":{\\\"type\\\":\\\"custom\\\",\\\"filter\\\":[\\\"lowercase\\\"]}},\\\"analyzer\\\":{\\\"default\\\":{\\\"tokenizer\\\":\\\"standard\\\",\\\"filter\\\":[\\\"standard\\\",\\\"lowercase\\\",\\\"porter_stem\\\"],\\\"char_filter\\\":[\\\"special_char_replace_filter\\\"]},\\\"default_ar\\\":{\\\"filter\\\":[\\\"standard\\\",\\\"stemmer_ar\\\"],\\\"type\\\":\\\"custom\\\",\\\"tokenizer\\\":\\\"standard\\\"},\\\"shingle_analyzer_ar\\\":{\\\"type\\\":\\\"custom\\\",\\\"tokenizer\\\":\\\"standard\\\",\\\"filter\\\":[\\\"standard\\\",\\\"stemmer_ar\\\",\\\"shingle\\\"]},\\\"shingle_analyzer\\\":{\\\"type\\\":\\\"custom\\\",\\\"tokenizer\\\":\\\"standard\\\",\\\"filter\\\":[\\\"standard\\\",\\\"lowercase\\\",\\\"porter_stem\\\",\\\"shingle\\\"],\\\"char_filter\\\":[\\\"special_char_replace_filter\\\"]},\\\"keyword_analyzer\\\":{\\\"type\\\":\\\"custom\\\",\\\"tokenizer\\\":\\\"keyword\\\",\\\"filter\\\":[\\\"lowercase\\\"]},\\\"whitespace_analyzer\\\":{\\\"type\\\":\\\"custom\\\",\\\"tokenizer\\\":\\\"whitespace\\\",\\\"filter\\\":[\\\"lowercase\\\"]}}}}}";
        System.out.println(str1.length());
        System.out.println(str1.getBytes().length);
        System.out.println("123");
        System.out.println("4565xx");
        System.out.println(2 << (16-1));//tcp client port unsigned short=2byte
        System.out.println(2 << 48);//max tcp server
    }

}
