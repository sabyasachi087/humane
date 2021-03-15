package com.sroyc.humane.services;

import com.sroyc.humane.data.model.HumaneEvent;
import com.sroyc.humane.data.model.HumaneEvent.EventType;

public interface HumaneEventListener {

	void listen(HumaneEvent event);

	boolean isEventSupported(EventType type);

}
