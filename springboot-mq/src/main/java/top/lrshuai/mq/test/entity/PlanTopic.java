package top.lrshuai.mq.test.entity;

public enum PlanTopic {
    PLAN_PLUCK("pluck"),
    PLAN_RELESE("relese"),
    PLAN_TRANSFER("transfer"),
    ;
    private String value;
    public String getValue() {
        return value;
    }
    private PlanTopic(String value){
        this.value=value;
    }
}
