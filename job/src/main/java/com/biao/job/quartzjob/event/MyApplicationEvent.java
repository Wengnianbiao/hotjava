package com.biao.job.quartzjob.event;

import org.springframework.context.ApplicationEvent;

public class MyApplicationEvent extends ApplicationEvent {

    private String msg;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MyApplicationEvent(Object source) {
        super(source);
    }

    public MyApplicationEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public void printMsg() {
        System.out.println("msg:" + msg);
    }
}
