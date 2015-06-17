package jdepend.metadata.annotation;

import java.io.Serializable;

public class AnnotationDefs implements Serializable {

	private static final long serialVersionUID = 8673294114544812064L;

	private Transactional transactional;

	private RequestMapping requestMapping;

	private Controller controller;

	private Service service;

	public Transactional getTransactional() {
		return transactional;
	}

	public void setTransactional(Transactional transactional) {
		this.transactional = transactional;
	}

	public RequestMapping getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
