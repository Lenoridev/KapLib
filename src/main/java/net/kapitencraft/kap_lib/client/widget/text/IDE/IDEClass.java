package net.kapitencraft.kap_lib.client.widget.text.IDE;

import java.util.List;

public class IDEClass extends IDEObject {
    public List<IDEMethod> methods;
    List<IDEVar> variables;

    public List<IDEVar> getVariables() {
        return variables;
    }

    public List<IDEMethod> getMethods() {
        return methods;
    }
}
