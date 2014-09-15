package com.github.nuclearg.kyou.dom.serialize;

import java.util.Map;
import java.util.Stack;

import com.github.nuclearg.kyou.dom.KyouArray;
import com.github.nuclearg.kyou.dom.KyouContainer;
import com.github.nuclearg.kyou.dom.KyouDocument;
import com.github.nuclearg.kyou.dom.KyouField;
import com.github.nuclearg.kyou.dom.KyouItem;
import com.github.nuclearg.kyou.dom.KyouStruct;

/**
 * 工具类 用于根据需求建造出一棵DOM树
 * <p>
 * 结构和域的构建过程非常普通，数组的构建过程比较复杂。数组的prototype是新起了一个DomBuilder来实现的。
 * </p>
 * 
 * @author ng
 */
class DomBuilder {
    /**
     * 当前正被构造的{@link KyouDocument}对象
     */
    private KyouDocument doc;
    /**
     * 当前构建栈
     */
    private Stack<KyouContainer> stack = new Stack<>();
    /**
     * 构建数组原型的builder
     */
    private DomBuilder prototypeBuilder;
    /**
     * 如果当前{@link DomBuilder}正在用于构建某个数组的原型，则此字段保存数组本身的名称
     */
    private String prototypeName;

    /**
     * 当前构建的域
     */
    private KyouField currentField;

    /**
     * 构建一个{@link KyouDocument}
     * 
     * @param body
     * @param attributes
     */
    void beginDocument(Map<String, String> attributes) {
        this.doc = new KyouDocument();
        this.doc.attributes().putAll(attributes);

        this.stack.push(this.doc);
    }

    /**
     * 完成整个{@link KyouDocument}的构建
     * 
     * @return 构建好的整篇报文数据
     */
    KyouDocument endDocument() {
        return this.doc;
    }

    /**
     * 构建一个{@link KyouStruct}
     * 
     * @param name
     *            名称
     * @param attributes
     *            该结构元素的属性
     */
    void beginStruct(String name, Map<String, String> attributes) {
        if (this.prototypeBuilder != null) {
            this.prototypeBuilder.beginStruct(name, attributes);
            return;
        }

        KyouStruct stru;
        KyouContainer parent = this.stack.peek();
        if (parent instanceof KyouStruct) {
            KyouStruct parentStru = (KyouStruct) parent;

            stru = (KyouStruct) parentStru.get(name);
            if (stru == null) {
                stru = new KyouStruct();
                stru.attributes().putAll(attributes);
                parentStru.add(name, stru);
            }
        } else {
            KyouArray parentArray = (KyouArray) parent;

            stru = (KyouStruct) parentArray.add();
        }

        this.stack.push(stru);
    }

    /**
     * 关闭当前最外面的{@link KyouStruct}
     */
    void endStruct() {
        if (this.prototypeBuilder != null) {
            this.prototypeBuilder.endStruct();
            return;
        }

        this.stack.pop();
    }

    /**
     * 构建一个{@link KyouField}
     * 
     * @param attributes
     *            该域元素的属性
     */
    void beginField(String name, Map<String, String> attributes) {
        if (this.prototypeBuilder != null) {
            this.prototypeBuilder.beginField(name, attributes);
            return;
        }

        KyouField field;
        KyouContainer parent = this.stack.peek();
        if (parent instanceof KyouStruct) {
            KyouStruct parentStru = (KyouStruct) parent;

            field = (KyouField) parentStru.get(name);

            if (field == null) {
                field = new KyouField();
                field.attributes().putAll(attributes);

                parentStru.add(name, field);
            }
        } else {
            KyouArray parentArray = (KyouArray) parent;

            field = (KyouField) parentArray.add();
        }

        this.currentField = field;
    }

    /**
     * 关闭当前的{@link KyouField}
     * 
     * @param value
     *            元素的值
     */
    void endField(String value) {
        if (this.prototypeBuilder != null) {
            this.prototypeBuilder.endField(value);
            return;
        }

        this.currentField.value(value);
    }

    /**
     * 构建一个数组
     * 
     * @param name
     *            名称
     * @param attributes
     *            属性列表
     */
    void beginArray(String name, Map<String, String> attributes) {
        if (this.prototypeBuilder != null) {
            this.prototypeBuilder.beginArray(name, attributes);
            return;
        }

        KyouContainer parent = this.stack.peek();
        if (parent instanceof KyouStruct) {
            this.currentField = new KyouField();

            this.prototypeBuilder = new DomBuilder();
            this.prototypeBuilder.beginDocument(attributes);
            this.prototypeBuilder.prototypeName = name;
        } else {
            KyouArray parentArray = (KyouArray) this.stack.peek();

            KyouArray array = (KyouArray) parentArray.add();

            this.stack.push(array);
        }
    }

    /**
     * 关闭当前最外面的{@link KyouArray}
     */
    void endArray() {
        if (this.prototypeBuilder != null) {
            this.prototypeBuilder.endArray();
            return;
        }

        this.stack.pop();
    }

    /**
     * 声明下一个构建的元素是当前数组的原型
     */
    void beginArrayPrototype() {
        if (this.prototypeBuilder != null && this.prototypeBuilder.prototypeBuilder != null) {
            this.prototypeBuilder.beginArrayPrototype();
            return;
        }
    }

    /**
     * 结束数组原型构建
     */
    void endArrayPrototype() {
        if (this.prototypeBuilder != null && this.prototypeBuilder.prototypeBuilder != null) {
            this.prototypeBuilder.endArrayPrototype();
            return;
        }

        KyouDocument prototypeDoc = this.prototypeBuilder.doc;
        KyouItem prototype = prototypeDoc.get(0);

        KyouArray array = new KyouArray(prototype);
        array.attributes().putAll(prototypeDoc.attributes());

        KyouStruct parentStru = (KyouStruct) this.stack.peek();
        parentStru.add(this.prototypeBuilder.prototypeName, array);

        this.stack.push(array);
        this.prototypeBuilder = null;
    }
}