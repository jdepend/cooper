package jdepend.model.profile.model.defaultvalue;

import java.util.ArrayList;
import java.util.List;

import jdepend.model.profile.model.AreaComponentProfile;

public class DefaultAreaComponentProfile extends AreaComponentProfile {

	public DefaultAreaComponentProfile() {
		this.setCreate(true);

		List<String> accordings = new ArrayList<String>();
		accordings.add(AccordingComponentLayer);
		accordings.add(AccordingInstability);

		this.setAccordings(accordings);
	}

}
