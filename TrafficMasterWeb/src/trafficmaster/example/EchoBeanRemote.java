package trafficmaster.example;

import javax.ejb.Remote;

@Remote
public interface EchoBeanRemote {

		public String echo(String message);
}
