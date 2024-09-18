package net.kapitencraft.kap_lib.client.widget.text.IDE;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class IDEMethod extends IDEObject {
    public IDEClass returnType;
    public Map<String, IDEClass> parameters = new LinkedHashMap<>();
}
