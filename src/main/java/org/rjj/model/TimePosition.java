package org.rjj.model;

import com.ib.controller.Position;

import java.util.Date;

public class TimePosition {

    private Date positionDateTime;
    private Position position;

    public TimePosition(Date positionTime, Position position) {

        this.positionDateTime = positionTime;
        this.position = position;
    }

    @Override
    public String toString() {
        return "TimePosition{" +
                "positionDateTime=" + positionDateTime +
                ", position={symbol=" + position.contract().symbol() +
                ", id=" + position.contract().conid() +
                ", exchange=" + position.contract().exchange() +
                '}';
    }
}
