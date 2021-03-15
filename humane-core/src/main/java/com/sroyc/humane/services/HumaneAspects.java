package com.sroyc.humane.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sroyc.humane.data.model.HumaneEvent;
import com.sroyc.humane.data.model.HumaneUserMaster;
import com.sroyc.humane.data.model.HumaneEvent.EventType;
import com.sroyc.humane.view.models.HumaneUserMasterVO;

@Aspect
@Component
public class HumaneAspects {

	private static final Logger LOGGER = LogManager.getLogger(HumaneAspects.class);

	@Autowired
	private HumaneEventPublisher publisher;

	@AfterReturning(pointcut = "within(com.sroyc.humane.data.dao.HumaneUserMasterRepository+) && execution(* createUser(..))", returning = "hum")
	public void createUserAspect(HumaneUserMaster hum) {
		Assert.notNull(hum, "Returning user object cannot be null");
		HumaneEvent event = new HumaneEvent();
		event.setData(new HumaneUserMasterVO(hum));
		event.setEvent(EventType.USER_CREATED);
		event.setDataType(HumaneUserMasterVO.class);
		this.publisher.publish(event);
		LOGGER.info("User created event has been published for user id [{}]", hum.getUserId());
	}

}
