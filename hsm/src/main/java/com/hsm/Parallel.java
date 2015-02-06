package com.hsm;

import java.util.Arrays;
import java.util.List;

public class Parallel extends State<Parallel> {

    private final List<StateMachine> mStateMachineList;


    public Parallel(String id, StateMachine... stateMachines) {
        super(id);
        mStateMachineList = Arrays.asList(stateMachines);
    }

    @Override
    protected Parallel getThis() {
        return this;
    }

    @Override
    void enter(State prev, State next) {
        super.enter(prev, next);
        for(StateMachine stateMachine : mStateMachineList) {
            stateMachine.init();
        }
    }

    @Override
    void exit(State prev, State next) {
        super.exit(prev, next);
        for(StateMachine stateMachine : mStateMachineList) {
            stateMachine.teardown();
        }
    }

    @Override
    boolean handleWithOverride(Event event) {
        boolean isHandled = false;
        for(StateMachine stateMachine : mStateMachineList) {
            if(stateMachine.handleWithOverride(event)) {
                isHandled = true;
            }
        }
        if(!isHandled) {
            return super.handleWithOverride(event);
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId());
        sb.append("/(");
        for (StateMachine stateMachine : mStateMachineList) {
            sb.append(stateMachine.toString());
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    @Override
    void addParent(StateMachine stateMachine) {
        for(StateMachine machine : mStateMachineList) {
            machine.addParent(stateMachine);
        }
    }
}
