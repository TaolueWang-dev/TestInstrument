/* Usage: java MainDriver [soot-options] appClass
*/

/* import necessary soot packages */
import soot.*;
import soot.options.Options;

public class MainDriver {
    public static void main(String[] args) {
        /* check the arguments */
        if (args.length == 0) {
            System.err.println("Usage: java MainDriver [options] classname");
            System.exit(0);
        }
        String classPath = "input";
        String rtJar = System.getenv("JAVA_HOME") + "/jre/lib/rt.jar";
        Options.v().set_soot_classpath(classPath + ":" + rtJar);
        /* add a phase to transformer pack by call Pack.add */
        Pack jtp = PackManager.v().getPack("jtp");
        jtp.add(new Transform("jtp.instrumenter", new GotoCounterTransformer()));
        soot.Main.main(args);
        }


    }

