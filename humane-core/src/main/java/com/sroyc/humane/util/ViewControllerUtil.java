package com.sroyc.humane.util;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import com.sroyc.humane.view.models.GenericPageableResponseItems;
import com.sroyc.humane.view.models.GenericResponse;
import com.sroyc.humane.view.models.GenericResponse.ResponseStatus;
import com.sroyc.humane.view.models.GenericResponseItems;

public class ViewControllerUtil {

	private ViewControllerUtil() {
	}

	public static final <T extends Serializable> ResponseEntity<Serializable> create(String message, HttpStatus status,
			T body) {
		GenericResponse<T> resp = new GenericResponse<>();
		resp.setData(body);
		resp.setMessage(message);
		resp.setStatus(status.isError() ? ResponseStatus.ERROR : ResponseStatus.SUCCESS);
		return new ResponseEntity<>(resp, status);
	}

	public static final ResponseEntity<Serializable> create(String message, HttpStatus status) {
		GenericResponse<Serializable> resp = new GenericResponse<>();
		resp.setMessage(message);
		resp.setStatus(status.isError() ? ResponseStatus.ERROR : ResponseStatus.SUCCESS);
		return new ResponseEntity<>(resp, status);
	}

	public static final <T extends Collection<?>> ResponseEntity<Serializable> create(T items, Integer page,
			Integer pageSize, String message, HttpStatus status) {
		GenericResponse<Serializable> resp = new GenericResponse<>();
		resp.setMessage(message);
		resp.setStatus(status.isError() ? ResponseStatus.ERROR : ResponseStatus.SUCCESS);
		resp.setData(create(items, page, pageSize));
		return new ResponseEntity<>(resp, status);
	}

	public static final <T extends Collection<?>> ResponseEntity<Serializable> create(T items, String message,
			HttpStatus status) {
		GenericResponse<Serializable> resp = new GenericResponse<>();
		resp.setMessage(message);
		resp.setStatus(status.isError() ? ResponseStatus.ERROR : ResponseStatus.SUCCESS);
		resp.setData(create(items));
		return new ResponseEntity<>(resp, status);
	}

	public static final <T extends Collection<?>> GenericPageableResponseItems create(T items, Integer page,
			Integer pageSize) {
		GenericPageableResponseItems pageableResponse = new GenericPageableResponseItems();
		pageableResponse.setItems(items);
		pageableResponse.setPage(page);
		if (!CollectionUtils.isEmpty(items)) {
			pageableResponse.setHasMoreItems(items.size() < pageSize);
		}
		return pageableResponse;
	}

	public static final <T extends Collection<?>> GenericResponseItems<T> create(T items) {
		GenericResponseItems<T> response = new GenericResponseItems<>();
		response.setItems(items);
		return response;
	}

}
