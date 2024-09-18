package net.kapitencraft.kap_lib.client.widget.text.IDE;

public enum IDECompilerStates {
    NewLine, // The cursor is directly after a semicolon
    ExpectingPrimaryModifierKeyword, // Words such as "public" or "private" are expected
    ExpectingSecondaryModifierKeyword, // Words such as "static", "final" or "transient" are expected
    ExpectingTertiaryModifierKeyword, // Words such as "transient" are expected
    ExpectingVar, // A variable reference is expected
    ExpectingVarName, // A name for a variable declaration is expected
    ExpectingClass, // A class name reference is expected
    ExpectingClassName, // A name for a variable declaration is expected
    ExpectingMethodCall, // A Method call is expected
    ExpectingMethodName, // A name for a method declaration is expected
    ExpectingMethodArguments // Method arguments in a method call are expected
}
