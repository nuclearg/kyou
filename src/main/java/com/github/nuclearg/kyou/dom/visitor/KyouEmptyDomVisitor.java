package com.github.nuclearg.kyou.dom.visitor;

import com.github.nuclearg.kyou.dom.KyouArray;
import com.github.nuclearg.kyou.dom.KyouDocument;
import com.github.nuclearg.kyou.dom.KyouField;
import com.github.nuclearg.kyou.dom.KyouStruct;

public class KyouEmptyDomVisitor implements KyouDomVisitor {

    @Override
    public void docStart(KyouDocument doc) {
    }

    @Override
    public void docEnd(KyouDocument doc) {
    }

    @Override
    public void struStart(KyouStruct stru) {
    }

    @Override
    public void struEnd(KyouStruct stru) {
    }

    @Override
    public void arrayStart(KyouArray array) {
    }

    @Override
    public void arrayEnd(KyouArray array) {
    }

    @Override
    public void field(KyouField field) {
    }

}
