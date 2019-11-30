package connectivity;

import DDS.*;
import MissionData.AgentActionTypeSupportImpl;
import OpenDDS.DCPS.DEFAULT_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;
import OpenDDS.DCPS.TheServiceParticipant;
import environment.Point;
import environment.PointParser;
import interfaces.ISubscribedDataReceivedListener;
import org.omg.CORBA.StringSeqHolder;

import java.util.List;

public class Subscribe implements ISubscribedDataReceivedListener{

//    private DomainParticipantFactory domainParticipantFactory;
//    private DomainParticipant domainParticipant;
//    private Topic topic;
//    private AgentActionTypeSupportImpl agentActionTypeSupport;
//    private DataReader dataReader;
//    private DataReaderQos dr_qos;
//    private DataReaderQosHolder qosh;
//    private Subscriber subscriber;
//    private static Subscribe subscribe;
//    private boolean newDataReceived;
//    /*
//    Initiate the object of domainParticipant Factory ,Domain Participant,topic,publisher,
//    Default transport protocol
//     */
//    public static synchronized Subscribe getInstance(){
//        if(subscribe == null){
//            subscribe =new Subscribe();
//        }
//        return subscribe;
//    }
//    public void start(String args[]){
//
//        System.out.println("Start Subscriber");
//
//        //Get the instance of domain Participant Factory
//        domainParticipantFactory =
//                TheParticipantFactory.WithArgs(new StringSeqHolder(args));
//        if (domainParticipantFactory == null) {
//            System.err.println("ERROR: Domain Participant Factory not found");
//            return;
//        }
//
//        //Create the instance of domain participant
//        domainParticipant = domainParticipantFactory.create_participant(10,
//                PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);
//        if (domainParticipant == null) {
//            System.err.println("ERROR: Domain Participant creation failed");
//            return;
//        }
//
//        //Create the intance of datatype to be received
//        agentActionTypeSupport = new AgentActionTypeSupportImpl();
//        if (agentActionTypeSupport.register_type(domainParticipant, "") != RETCODE_OK.value) {
//            System.err.println("ERROR: register_type failed");
//            return;
//        }
//
//        //Create the topic
//        topic = domainParticipant.create_topic("Way Points",
//                agentActionTypeSupport.get_type_name(),
//                TOPIC_QOS_DEFAULT.get(),
//                null,
//                DEFAULT_STATUS_MASK.value);
//        if (topic == null) {
//            System.err.println("ERROR: Topic creation failed");
//            return;
//        }
//
//        //Create the subscriber
//        subscriber = domainParticipant.create_subscriber(SUBSCRIBER_QOS_DEFAULT.get(),
//                null, DEFAULT_STATUS_MASK.value);
//        if (subscriber == null) {
//            System.err.println("ERROR: Subscriber creation failed");
//            return;
//        }
//
//        // Use the default transport (do nothing)
//
//        dr_qos = new DataReaderQos();
//        dr_qos.durability = new DurabilityQosPolicy();
//        dr_qos.durability.kind = DurabilityQosPolicyKind.from_int(0);
//        dr_qos.deadline = new DeadlineQosPolicy();
//        dr_qos.deadline.period = new Duration_t();
//        dr_qos.latency_budget = new LatencyBudgetQosPolicy();
//        dr_qos.latency_budget.duration = new Duration_t();
//        dr_qos.liveliness = new LivelinessQosPolicy();
//        dr_qos.liveliness.kind = LivelinessQosPolicyKind.from_int(0);
//        dr_qos.liveliness.lease_duration = new Duration_t();
//        dr_qos.reliability = new ReliabilityQosPolicy();
//        dr_qos.reliability.kind = ReliabilityQosPolicyKind.from_int(0);
//        dr_qos.reliability.max_blocking_time = new Duration_t();
//        dr_qos.destination_order = new DestinationOrderQosPolicy();
//        dr_qos.destination_order.kind = DestinationOrderQosPolicyKind.from_int(0);
//        dr_qos.history = new HistoryQosPolicy();
//        dr_qos.history.kind = HistoryQosPolicyKind.from_int(0);
//        dr_qos.resource_limits = new ResourceLimitsQosPolicy();
//        dr_qos.user_data = new UserDataQosPolicy();
//        dr_qos.user_data.value = new byte[0];
//        dr_qos.ownership = new OwnershipQosPolicy();
//        dr_qos.ownership.kind = OwnershipQosPolicyKind.from_int(0);
//        dr_qos.time_based_filter = new TimeBasedFilterQosPolicy();
//        dr_qos.time_based_filter.minimum_separation = new Duration_t();
//        dr_qos.reader_data_lifecycle = new ReaderDataLifecycleQosPolicy();
//        dr_qos.reader_data_lifecycle.autopurge_nowriter_samples_delay = new Duration_t();
//        dr_qos.reader_data_lifecycle.autopurge_disposed_samples_delay = new Duration_t();
//
//        qosh = new DataReaderQosHolder(dr_qos);
//        subscriber.get_default_datareader_qos(qosh);
//        qosh.value.history.kind = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;
//
//        //Create the object of listner
//        DataReaderListenerImpl listener = new DataReaderListenerImpl();
//        //Create the instance of datareader
//        dataReader = subscriber.create_datareader(topic,
//                qosh.value,
//                listener,
//                DEFAULT_STATUS_MASK.value);
//        if (dataReader == null) {
//            System.err.println("ERROR: DataReader creation failed");
//            return;
//        }
//
//        StatusCondition sc = dataReader.get_statuscondition();
//        sc.set_enabled_statuses(SUBSCRIPTION_MATCHED_STATUS.value);
//        WaitSet ws = new WaitSet();
//        ws.attach_condition(sc);
//        SubscriptionMatchedStatusHolder matched =
//                new SubscriptionMatchedStatusHolder(new SubscriptionMatchedStatus());
//        Duration_t timeout = new Duration_t(DURATION_INFINITE_SEC.value,
//                DURATION_INFINITE_NSEC.value);
//
//        boolean matched_pub = false;
//
//        while (true) {
//            System.out.println("Inside while loop");
//            final int result = dataReader.get_subscription_matched_status(matched);
//            if (result != RETCODE_OK.value) {
//                System.err.println("ERROR: get_subscription_matched_status()" +
//                        "failed.");
//                return;
//            }
//            else if (matched.value.current_count > 0 &&
//                    !matched_pub) {
//                System.out.println("Subscriber Matched");
//                matched_pub = true;
//            }
//
//            ConditionSeqHolder cond = new ConditionSeqHolder(new Condition[]{});
//            if (ws.wait(cond, timeout) != RETCODE_OK.value) {
//                System.err.println("ERROR: wait() failed.");
//                return;
//            }
//        }
//
//    }
//
//    public void tearDown(){
//        System.out.println("Stop Subscriber");
//        domainParticipant.delete_contained_entities();
//        domainParticipantFactory.delete_participant(domainParticipant);
//        TheServiceParticipant.shutdown();
//
//        System.out.println("Subscriber exiting");
//
//    }
//
//    public boolean isNewDataReceived(){
//        return newDataReceived;
//    }
//    public void setNewDataReceived(boolean b){
//        this.newDataReceived = b;
//    }

    private DomainParticipantFactory domainParticipantFactory;
    private DomainParticipant domainParticipant;
    private Topic topic;
    private AgentActionTypeSupportImpl agentActionTypeSupport;
    private DataReader dataReader;
    private DataReaderQos dr_qos;
    private DataReaderQosHolder qosh;
    private Subscriber subscriber;
    //private static Subscribe subscribe;
    private boolean newDataReceived;
    private List<Point> points;
    /*
    Initiate the object of domainParticipant Factory ,Domain Participant,topic,publisher,
    Default transport protocol
     */
//    public static synchronized Subscribe getInstance(){
//        if(subscribe == null){
//            subscribe =new Subscribe();
//        }
//        return subscribe;
//    }
    public void start(String args[]){

        System.out.println("Start Subscriber");

        //Get the instance of domain Participant Factory
        domainParticipantFactory =
                TheParticipantFactory.WithArgs(new StringSeqHolder(args));
        if (domainParticipantFactory == null) {
            System.err.println("ERROR: Domain Participant Factory not found");
            return;
        }

        //Create the instance of domain participant
        domainParticipant = domainParticipantFactory.create_participant(10,
                PARTICIPANT_QOS_DEFAULT.get(), null, DEFAULT_STATUS_MASK.value);
        if (domainParticipant == null) {
            System.err.println("ERROR: Domain Participant creation failed");
            return;
        }

        //Create the intance of datatype to be received
        agentActionTypeSupport = new AgentActionTypeSupportImpl();
        if (agentActionTypeSupport.register_type(domainParticipant, "") != RETCODE_OK.value) {
            System.err.println("ERROR: register_type failed");
            return;
        }

        //Create the topic
        topic = domainParticipant.create_topic("Way Points",
                agentActionTypeSupport.get_type_name(),
                TOPIC_QOS_DEFAULT.get(),
                null,
                DEFAULT_STATUS_MASK.value);
        if (topic == null) {
            System.err.println("ERROR: Topic creation failed");
            return;
        }

        //Create the subscriber
        subscriber = domainParticipant.create_subscriber(SUBSCRIBER_QOS_DEFAULT.get(),
                null, DEFAULT_STATUS_MASK.value);
        if (subscriber == null) {
            System.err.println("ERROR: Subscriber creation failed");
            return;
        }

        // Use the default transport (do nothing)

        dr_qos = new DataReaderQos();
        dr_qos.durability = new DurabilityQosPolicy();
        dr_qos.durability.kind = DurabilityQosPolicyKind.from_int(0);
        dr_qos.deadline = new DeadlineQosPolicy();
        dr_qos.deadline.period = new Duration_t();
        dr_qos.latency_budget = new LatencyBudgetQosPolicy();
        dr_qos.latency_budget.duration = new Duration_t();
        dr_qos.liveliness = new LivelinessQosPolicy();
        dr_qos.liveliness.kind = LivelinessQosPolicyKind.from_int(0);
        dr_qos.liveliness.lease_duration = new Duration_t();
        dr_qos.reliability = new ReliabilityQosPolicy();
        dr_qos.reliability.kind = ReliabilityQosPolicyKind.from_int(0);
        dr_qos.reliability.max_blocking_time = new Duration_t();
        dr_qos.destination_order = new DestinationOrderQosPolicy();
        dr_qos.destination_order.kind = DestinationOrderQosPolicyKind.from_int(0);
        dr_qos.history = new HistoryQosPolicy();
        dr_qos.history.kind = HistoryQosPolicyKind.from_int(0);
        dr_qos.resource_limits = new ResourceLimitsQosPolicy();
        dr_qos.user_data = new UserDataQosPolicy();
        dr_qos.user_data.value = new byte[0];
        dr_qos.ownership = new OwnershipQosPolicy();
        dr_qos.ownership.kind = OwnershipQosPolicyKind.from_int(0);
        dr_qos.time_based_filter = new TimeBasedFilterQosPolicy();
        dr_qos.time_based_filter.minimum_separation = new Duration_t();
        dr_qos.reader_data_lifecycle = new ReaderDataLifecycleQosPolicy();
        dr_qos.reader_data_lifecycle.autopurge_nowriter_samples_delay = new Duration_t();
        dr_qos.reader_data_lifecycle.autopurge_disposed_samples_delay = new Duration_t();

        qosh = new DataReaderQosHolder(dr_qos);
        subscriber.get_default_datareader_qos(qosh);
        qosh.value.history.kind = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;

        //Create the object of listner
        DataReaderListenerImpl listener = new DataReaderListenerImpl(this);
        //Create the instance of datareader
        dataReader = subscriber.create_datareader(topic,
                qosh.value,
                listener,
                DEFAULT_STATUS_MASK.value);
        if (dataReader == null) {
            System.err.println("ERROR: DataReader creation failed");
            return;
        }

        StatusCondition sc = dataReader.get_statuscondition();
        sc.set_enabled_statuses(SUBSCRIPTION_MATCHED_STATUS.value);
        WaitSet ws = new WaitSet();
        ws.attach_condition(sc);
        SubscriptionMatchedStatusHolder matched =
                new SubscriptionMatchedStatusHolder(new SubscriptionMatchedStatus());
        Duration_t timeout = new Duration_t(DURATION_INFINITE_SEC.value,
                DURATION_INFINITE_NSEC.value);

        boolean matched_pub = false;

        while (true) {
            System.out.println("Inside while loop");
            final int result = dataReader.get_subscription_matched_status(matched);
            if (result != RETCODE_OK.value) {
                System.err.println("ERROR: get_subscription_matched_status()" +
                        "failed.");
                return;
            }
            else if (matched.value.current_count > 0 &&
                    !matched_pub) {
                System.out.println("Subscriber Matched");
                matched_pub = true;
            }

            ConditionSeqHolder cond = new ConditionSeqHolder(new Condition[]{});
            if (ws.wait(cond, timeout) != RETCODE_OK.value) {
                System.err.println("ERROR: wait() failed.");
                return;
            }
        }

    }

    public void tearDown(){
        System.out.println("Stop Subscriber");
        domainParticipant.delete_contained_entities();
        domainParticipantFactory.delete_participant(domainParticipant);
        TheServiceParticipant.shutdown();

        System.out.println("Subscriber exiting");

    }

    public boolean isNewDataReceived(){
        return newDataReceived;
    }
    public void setNewDataReceived(boolean b){
        this.newDataReceived = b;
    }
    public List<Point> getPoints(){
        return points;
    }

    @Override
    public void onSubscribedDataReceived(String[] data) {

        PointParser pointParser = new PointParser(data);

        points = pointParser.getPoints();
        System.out.println(points.get(1).getX());
        newDataReceived = true;

    }
}
