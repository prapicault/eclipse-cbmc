package commands;

import org.eclipse.emf.common.util.EList;

import infra.MICommand;
import infra.MIOutput;
import process.Process;
import process.Thread;
import results.data.MIThread;
import results.data.MIThreadGroup;
import results.sync.Done;
import results.sync.Error;

public class ListThreadGroups extends MICommand {

	@Override
	public MIOutput perform(Process process) {
		
		if (parameters.length() == 0 || parameters.equals("--available")) {
			MIThreadGroup threadGroup = new MIThreadGroup();
			threadGroup.id = process.getGroupThreadId();  
			threadGroup.pid = process.getProcessNumber();
			threadGroup.type = "process";
			threadGroup.executable = process.getExecutableName();
			return new Done(this, "groups", new Object[] {threadGroup});
		}
		
		if (!process.isStarted())
			return new Error(this, "Application not started. Can't fetch data now");
		
		if (parameters.equals(process.getGroupThreadId())) {
			EList<Thread> activeThreads = process.getActiveThreads();
			MIThread[] activeThreadsResponse = new MIThread[activeThreads.size()];
			int i = 0;
			for (Thread thread : activeThreads) {
				activeThreadsResponse[i] = new MIThread(thread);
			}
			return new Done(this, "threads", activeThreadsResponse);
		}
		return new Error(this, "Unsupported command option or unmatched parameter " + parameters);
	}
}
