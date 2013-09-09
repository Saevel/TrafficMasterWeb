package trafficmaster.example;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class EchoBean
 */
@Stateless
public class EchoBean implements EchoBeanRemote {

	@Override
	public String echo(String message) {
		return message;
	}

}
