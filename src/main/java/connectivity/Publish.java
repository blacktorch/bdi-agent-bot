package connectivity;

import DDS.*;
import MissionData.*;
import OpenDDS.DCPS.DEFAULT_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;
import OpenDDS.DCPS.TheServiceParticipant;
import org.omg.CORBA.StringSeqHolder;

public class Publish {

    private DomainParticipantFactory domainParticipantFactory;
    private DomainParticipant domainParticipant;
    private DataWriter dataWriter;
    private String dataType;
    private String topicString;
    private int domainID;
    private int durabilityKind;
    private int historyKind;
    private int serviceHistoryKind;
    private int livelinessKind;
    private int reliabilityKind;
    private int orderKind;
    private int ownershipKind;
    private boolean matched = false;

    public Publish(int domainID, String topicString, String dataType, int durabilityKind, int historyKind,
                   int serviceHistoryKind, int livelinessKind, int reliabilityKind, int orderKind, int ownershipKind){
        this.dataType = dataType;
        this.topicString = topicString;
        this.domainID = domainID;
        this.durabilityKind = durabilityKind;
        this.historyKind = historyKind;
        this.serviceHistoryKind = serviceHistoryKind;
        this.livelinessKind = livelinessKind;
        this.reliabilityKind = reliabilityKind;
        this.orderKind = orderKind;
        this.ownershipKind = ownershipKind;
    }


    public static class Builder{
        private String dataType = "";
        private String topicString = "topic";
        private int domainID = 1;
        private int durabilityKind = 0;
        private int historyKind = 0;
        private int serviceHistoryKind = 0;
        private int livelinessKind = 0;
        private int reliabilityKind = 0;
        private int orderKind = 0;
        private int ownershipKind = 0;

        public Builder(){}

        public Builder setDataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder setTopicString(String topicString) {
            this.topicString = topicString;
            return this;
        }

        public Builder setDomainID(int domainID) {
            this.domainID = domainID;
            return this;
        }

        public Builder setDurabilityKind(int durabilityKind) {
            this.durabilityKind = durabilityKind;
            return this;
        }

        public Builder setHistoryKind(int historyKind) {
            this.historyKind = historyKind;
            return this;
        }

        public Builder setServiceHistoryKind(int serviceHistoryKind) {
            this.serviceHistoryKind = serviceHistoryKind;
            return this;
        }

        public Builder setLivelinessKind(int livelinessKind) {
            this.livelinessKind = livelinessKind;
            return this;
        }

        public Builder setReliabilityKind(int reliabilityKind) {
            this.reliabilityKind = reliabilityKind;
            return this;
        }

        public Builder setOrderKind(int orderKind) {
            this.orderKind = orderKind;
            return this;
        }

        public Builder setOwnershipKind(int ownershipKind) {
            this.ownershipKind = ownershipKind;
            return this;
        }

        public Publish build(){
            return new Publish(domainID, topicString, dataType, durabilityKind, historyKind, serviceHistoryKind, livelinessKind, reliabilityKind, orderKind, ownershipKind);
        }
    }


    /*
    Initiate the object of domainParticipant Factory ,Domain Participant,topic,publisher,
    Default transport protocol
     */
    public void setUp() {

        Topic topic = null;
        Publisher publisher;
        NormalizedPointsTypeSupportImpl normalizedPointsTypeSupport;
        MissionPlanTypeSupportImpl missionPlanTypeSupport;
        TelemetryTypeSupportImpl telemetryTypeSupport;
        DataWriterQos dw_qos;
        DataWriterQosHolder qosh;

        domainParticipantFactory = DDSBase.getInstance().getDomainParticipantFactory();

        //Create the domain Participant
        domainParticipant = DDSBase.getInstance().getDomainParticipant();

        switch (this.dataType){
            case "NormalizedPoints":
                normalizedPointsTypeSupport = new NormalizedPointsTypeSupportImpl();
                if (normalizedPointsTypeSupport.register_type(domainParticipant, "") != RETCODE_OK.value) {
                    System.err.println("ERROR: register_type failed");
                    return;
                }

                topic = domainParticipant.create_topic(this.topicString,
                        normalizedPointsTypeSupport.get_type_name(),
                        TOPIC_QOS_DEFAULT.get(),
                        null,
                        DEFAULT_STATUS_MASK.value);
                break;
            case "MissionPlan":
                missionPlanTypeSupport = new MissionPlanTypeSupportImpl();
                if (missionPlanTypeSupport.register_type(domainParticipant, "") != RETCODE_OK.value) {
                    System.err.println("ERROR: register_type failed");
                    return;
                }

                topic = domainParticipant.create_topic(this.topicString,
                        missionPlanTypeSupport.get_type_name(),
                        TOPIC_QOS_DEFAULT.get(),
                        null,
                        DEFAULT_STATUS_MASK.value);
                break;
            case "Telemetry":
                telemetryTypeSupport = new TelemetryTypeSupportImpl();
                if (telemetryTypeSupport.register_type(domainParticipant, "") != RETCODE_OK.value) {
                    System.err.println("ERROR: register_type failed");
                    return;
                }

                topic = domainParticipant.create_topic(this.topicString,
                        telemetryTypeSupport.get_type_name(),
                        TOPIC_QOS_DEFAULT.get(),
                        null,
                        DEFAULT_STATUS_MASK.value);
                break;
        }



        if (topic == null) {
            System.err.println("ERROR: Topic creation failed");
            return;
        }


        //Create Publisher
        publisher = domainParticipant.create_publisher(PUBLISHER_QOS_DEFAULT.get(), null,
                DEFAULT_STATUS_MASK.value);
        if (publisher == null) {
            System.err.println("ERROR: Publisher creation failed");
            return;
        }


        //Default Transport Protocol

        dw_qos = new DataWriterQos();
        dw_qos.durability = new DurabilityQosPolicy();
        dw_qos.durability.kind = DurabilityQosPolicyKind.from_int(this.durabilityKind);
        dw_qos.durability_service = new DurabilityServiceQosPolicy();
        dw_qos.durability_service.history_kind = HistoryQosPolicyKind.from_int(this.serviceHistoryKind);
        dw_qos.durability_service.service_cleanup_delay = new Duration_t();
        dw_qos.deadline = new DeadlineQosPolicy();
        dw_qos.deadline.period = new Duration_t();
        dw_qos.latency_budget = new LatencyBudgetQosPolicy();
        dw_qos.latency_budget.duration = new Duration_t();
        dw_qos.liveliness = new LivelinessQosPolicy();
        dw_qos.liveliness.kind = LivelinessQosPolicyKind.from_int(this.livelinessKind);
        dw_qos.liveliness.lease_duration = new Duration_t();
        dw_qos.reliability = new ReliabilityQosPolicy();
        dw_qos.reliability.kind = ReliabilityQosPolicyKind.from_int(this.reliabilityKind);
        dw_qos.reliability.max_blocking_time = new Duration_t();
        dw_qos.destination_order = new DestinationOrderQosPolicy();
        dw_qos.destination_order.kind = DestinationOrderQosPolicyKind.from_int(this.orderKind);
        dw_qos.history = new HistoryQosPolicy();
        dw_qos.history.kind = HistoryQosPolicyKind.from_int(this.historyKind);
        dw_qos.resource_limits = new ResourceLimitsQosPolicy();
        dw_qos.transport_priority = new TransportPriorityQosPolicy();
        dw_qos.lifespan = new LifespanQosPolicy();
        dw_qos.lifespan.duration = new Duration_t();
        dw_qos.user_data = new UserDataQosPolicy();
        dw_qos.user_data.value = new byte[0];
        dw_qos.ownership = new OwnershipQosPolicy();
        dw_qos.ownership.kind = OwnershipQosPolicyKind.from_int(this.ownershipKind);
        dw_qos.ownership_strength = new OwnershipStrengthQosPolicy();
        dw_qos.writer_data_lifecycle = new WriterDataLifecycleQosPolicy();

        qosh = new DataWriterQosHolder(dw_qos);
        publisher.get_default_datawriter_qos(qosh);
        qosh.value.history.kind = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;

        //Initiate DataWriter
        dataWriter = publisher.create_datawriter(topic, qosh.value, null, DEFAULT_STATUS_MASK.value);
        if (dataWriter == null) {
            System.err.println("ERROR: DataWriter creation failed");
            return;
        }
        System.out.println("Publisher Created DataWriter Message data");

        StatusCondition sc = dataWriter.get_statuscondition();
        sc.set_enabled_statuses(PUBLICATION_MATCHED_STATUS.value);
        WaitSet ws = new WaitSet();
        ws.attach_condition(sc);
        PublicationMatchedStatusHolder matched =
                new PublicationMatchedStatusHolder(new PublicationMatchedStatus());
        Duration_t timeout = new Duration_t(DURATION_INFINITE_SEC.value, DURATION_INFINITE_NSEC.value);

        while (true) {
            System.out.println("Waiting for publisher to match");
            final int result = dataWriter.get_publication_matched_status(matched);
            if (result != RETCODE_OK.value) {
                System.err.println("ERROR: get_publication_matched_status()" +
                        "failed.");
                this.matched = false;
                return;
            }

            if (matched.value.current_count >= 1) {
                this.matched = true;
                break;
            }

            ConditionSeqHolder cond = new ConditionSeqHolder(new Condition[]{});
            if (ws.wait(cond, timeout) != RETCODE_OK.value) {
                System.err.println("ERROR: wait() failed.");
                System.out.println("TIMEOUT");
                this.matched = false;
                return;

            }

        }

        ws.detach_condition(sc);
    }

    public boolean isMatched(){
        return this.matched;
    }

    public void write(String from, String[] data, int id) {

        try {

            int handle = -1;
            int ret = RETCODE_TIMEOUT.value;

            switch (this.dataType){
                case "NormalizedPoints":
                    NormalizedPointsDataWriter normalizedPointsDataWriter = NormalizedPointsDataWriterHelper.narrow(dataWriter);
                    NormalizedPoints normalizedPoints = new NormalizedPoints();
                    normalizedPoints.from = from;
                    normalizedPoints.pointsId = id;
                    normalizedPoints.points_data = new String[0];
                    normalizedPoints.points_data = data;
                    handle = normalizedPointsDataWriter.register_instance(normalizedPoints);
                    //write the data
                    ret = normalizedPointsDataWriter.write(normalizedPoints, handle);
                    break;
                case "MissionPlan":
                    MissionPlanDataWriter missionPlanDataWriter = MissionPlanDataWriterHelper.narrow(dataWriter);
                    MissionPlan missionPlan = new MissionPlan();
                    missionPlan.from = from;
                    missionPlan.planId = id;
                    missionPlan.obstacles = new String[0];
                    missionPlan.plan_data = new String[0];
                    missionPlan.plan_data = data;
                    handle = missionPlanDataWriter.register_instance(missionPlan);
                    //write the data
                    ret = missionPlanDataWriter.write(missionPlan, handle);

                    break;
                case "Telemetry":
                    TelemetryDataWriter telemetryDataWriter = TelemetryDataWriterHelper.narrow(dataWriter);
                    Telemetry telemetry = new Telemetry();
                    telemetry.from = from;
                    telemetry.telemetryId = id;
                    telemetry.telemetry_data = new String[0];
                    telemetry.telemetry_data = data;
                    telemetry.image = new byte[0];
                    handle = telemetryDataWriter.register_instance(telemetry);
                    //write the data
                    ret = telemetryDataWriter.write(telemetry, handle);

                    break;
            }


            //agentAction.action_data = new String[0];
            //String[] data = {"0,0", "1,0", "2,0", "3,0", "4,0", "4,-1", "4,-2", "4,-3", "4,-4"};


            if (ret != RETCODE_OK.value) {
                System.err.println("ERROR " +
                        " write() returned " + ret);
            }
            Thread.sleep(100);
        } catch (InterruptedException | NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    //Shutdown
    public void tearDown() {
        System.out.println("Stop the publisher");
        domainParticipant.delete_contained_entities();
        domainParticipantFactory.delete_participant(domainParticipant);
        TheServiceParticipant.shutdown();
        System.out.println("Publisher exiting");
    }

}