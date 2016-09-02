package jdepend.metadata.annotation;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ArrayElementValue;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.ElementValuePair;
import org.apache.bcel.classfile.EnumElementValue;
import org.apache.bcel.classfile.SimpleElementValue;

public class AnnotationParse {

	public static Controller parseController(AnnotationEntry annotationEntry) {
		Controller controller = new Controller();
		for (ElementValuePair elementValuePair : annotationEntry.getElementValuePairs()) {
			if (elementValuePair.getNameString().equals("value")) {
				SimpleElementValue simpleElementValue = (SimpleElementValue) elementValuePair.getValue();
				controller.setValue(simpleElementValue.toShortString());
			}
		}

		return controller;
	}

	public static Service parseService(AnnotationEntry annotationEntry) {
		Service service = new Service();
		for (ElementValuePair elementValuePair : annotationEntry.getElementValuePairs()) {
			if (elementValuePair.getNameString().equals("value")) {
				SimpleElementValue simpleElementValue = (SimpleElementValue) elementValuePair.getValue();
				service.setValue(simpleElementValue.toShortString());
			}
		}

		return service;
	}

	public static Qualifier parseQualifier(AnnotationEntry annotationEntry) {
		Qualifier qualifier = new Qualifier();
		for (ElementValuePair elementValuePair : annotationEntry.getElementValuePairs()) {
			if (elementValuePair.getNameString().equals("value")) {
				SimpleElementValue simpleElementValue = (SimpleElementValue) elementValuePair.getValue();
				qualifier.setValue(simpleElementValue.toShortString());
			}
		}

		return qualifier;
	}

	public static Autowired parseAutowired(AnnotationEntry annotationEntry) {
		Autowired autowired = new Autowired();
		for (ElementValuePair elementValuePair : annotationEntry.getElementValuePairs()) {
			if (elementValuePair.getNameString().equals("required")) {
				SimpleElementValue simpleElementValue = (SimpleElementValue) elementValuePair.getValue();
				autowired.setRequired(Boolean.valueOf(simpleElementValue.toShortString()));
			}
		}

		return autowired;
	}

	public static RequestMapping parseRequestMapping(AnnotationEntry annotationEntry) {
		RequestMapping requestMapping = new RequestMapping();
		for (ElementValuePair elementValuePair : annotationEntry.getElementValuePairs()) {
			if (elementValuePair.getNameString().equals("value")) {
				ArrayElementValue arrayElementValue = (ArrayElementValue) elementValuePair.getValue();
				for (ElementValue elementValue : arrayElementValue.getElementValuesArray()) {
					requestMapping.setValue(elementValue.toShortString());
				}
			} else if (elementValuePair.getNameString().equals("method")) {
				ArrayElementValue arrayElementValue = (ArrayElementValue) elementValuePair.getValue();
				for (ElementValue elementValue : arrayElementValue.getElementValuesArray()) {
					requestMapping.setMethod(elementValue.toShortString());
				}
			}
		}
		if (requestMapping.getValue() == null) {
			requestMapping.setValue("");
		}

		return requestMapping;
	}

	public static Transactional parseTransactional(AnnotationEntry annotationEntry) {
		Transactional transactional = new Transactional();
		for (ElementValuePair elementValuePair : annotationEntry.getElementValuePairs()) {
			if (elementValuePair.getNameString().equals("value")) {
				SimpleElementValue simpleElementValue = (SimpleElementValue) elementValuePair.getValue();
				transactional.setValue(simpleElementValue.toShortString());
			} else if (elementValuePair.getNameString().equals("readOnly")) {
				SimpleElementValue simpleElementValue = (SimpleElementValue) elementValuePair.getValue();
				transactional.setReadOnly(Boolean.valueOf(simpleElementValue.toShortString()));
			} else if (elementValuePair.getNameString().equals("propagation")) {
				EnumElementValue simpleElementValue = (EnumElementValue) elementValuePair.getValue();
				transactional.setPropagation(simpleElementValue.toShortString());
			}
		}

		return transactional;
	}

}
