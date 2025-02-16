import soot.*;
import soot.jimple.*;
import soot.util.*;
import java.util.*;

public class GotoCounterTransformer extends BodyTransformer {
    static SootClass counterClass;
    static SootMethod increaseCounter, reportCounter;

    static {
        counterClass    = Scene.v().loadClassAndSupport("MyCounter");
        increaseCounter = counterClass.getMethod("void increase(int)");
        reportCounter   = counterClass.getMethod("void report()");
    }
    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        Chain<Unit> units = body.getUnits();
        SootMethod method = body.getMethod();
//        Iterator<Unit> unitIterator = units.iterator();
        Iterator<Unit> stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Unit unit = stmtIt.next();
//            System.out.println("Unit type: " + unit.getClass().getName());
            if (unit instanceof GotoStmt) {
                System.out.println("Found Goto statement: " + unit);
                // take out the invoke expression
                InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(increaseCounter.makeRef(), IntConstant.v(1));
                Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
                // 将计数器更新语句插入到当前goto语句之前
                units.insertBefore(incStmt, unit);
            }

            String signature = method.getSubSignature();
            boolean isMain = signature.equals("void main(java.lang.String[])");
            // re-iterate the body to look for return statement
            if (isMain) {
                stmtIt = units.snapshotIterator();

                while (stmtIt.hasNext()) {
                    Stmt stmt = (Stmt)stmtIt.next();

                    // check if the instruction is a return with/without value
                    if ((stmt instanceof ReturnStmt) || (stmt instanceof ReturnVoidStmt)) {
                        // 1. make invoke expression of MyCounter.report()
                        InvokeExpr reportExpr = Jimple.v().newStaticInvokeExpr(reportCounter.makeRef());
                        // 2. then, make a invoke statement
                        Stmt reportStmt = Jimple.v().newInvokeStmt(reportExpr);

                        // 3. insert new statement into the chain
                        //    (we are mutating the unit chain).
                        units.insertBefore(reportStmt, stmt);
                    }
                }
            }
        }
    }

}
