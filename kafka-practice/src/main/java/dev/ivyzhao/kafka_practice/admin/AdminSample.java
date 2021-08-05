package dev.ivyzhao.kafka_practice.admin;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class AdminSample {

    public final static String TOPIC_NAME = "ivy_topic";

    public static void main(String[] args) throws Exception{
//        AdminClient adminClient = AdminSample.adminClient();
//        System.out.println("adminClient: " +adminClient);
        //create topics
//        createTopic();
        //get topic list
//        delTopics();
//        topicLists();
        describeTopics();
    }

    public static void describeTopics() throws Exception {
        AdminClient adminClient = adminClient();
        DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(TOPIC_NAME));
        Map<String, TopicDescription> stringTopicDescriptionMap = describeTopicsResult.all().get();
        Set<Map.Entry<String, TopicDescription>> entries = stringTopicDescriptionMap.entrySet();
        entries.stream().forEach((entry) -> {
            System.out.println("name: " + entry.getKey() + ", desc:" + entry.getValue());
        });
    }

    /**
     * delete topics
     */
    public static void delTopics() throws Exception{
        AdminClient adminClient = adminClient();
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(TOPIC_NAME));
        System.out.println("CreateTopicsResult: " + deleteTopicsResult.all().get());
    }

    public static void topicLists() throws Exception {
        AdminClient adminClient = adminClient();
        //whether check internal options
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(true);
//        ListTopicsResult listTopicsResult = adminClient.listTopics();
        ListTopicsResult listTopicsResult = adminClient.listTopics(options);
        Set<String> names = listTopicsResult.names().get();
        Collection<TopicListing> topicListings = listTopicsResult.listings().get();
        KafkaFuture<Map<String, TopicListing>> mapKafkaFuture = listTopicsResult.namesToListings();
        //print names
        names.stream().forEach(System.out::println);
        //print topicListings
        topicListings.stream().forEach((topicList) -> {
            System.out.println(topicList);
        });
    }

    /**
     * create Topic
     */
    public static void createTopic() {
        AdminClient adminClient = adminClient();
        //replication factor
        Short rs = 1;
        NewTopic newTopic = new NewTopic(TOPIC_NAME, 1, rs);
        CreateTopicsResult topics = adminClient.createTopics(Arrays.asList(newTopic));
        System.out.println("CreateTopicsResult : " + topics);
    }

    public static AdminClient adminClient() {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        AdminClient adminClient = AdminClient.create(properties);
        return adminClient;
    }
}
