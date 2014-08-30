package net.nuclearg.kyou.dom;

public class KyouDocument extends KyouStruct {
    @Override
    public String name() {
        return "[ROOT]";
    }

    @Override
    public KyouContainer parent() {
        return null;
    }
}
