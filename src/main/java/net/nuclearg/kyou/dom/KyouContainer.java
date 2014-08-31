package net.nuclearg.kyou.dom;

/**
 * 报文结构中的容器，包括数组和结构体
 * 
 * @author ng
 * 
 */
public abstract class KyouContainer extends KyouItem implements Iterable<KyouItem> {
    /**
     * 根据下标获取元素
     * 
     * @param index
     *            下标
     * @return 指定下标的元素，或为null
     */
    public abstract KyouItem get(int index);

    /**
     * 根据名称获取元素
     * 
     * @param name
     *            元素名称
     * @return 指定名称的元素，或为null
     */
    public abstract KyouItem get(String name);

    /**
     * 获取容器的大小
     */
    public abstract int size();

    /**
     * 判断容器是否为空
     */
    public abstract boolean isEmpty();

    /**
     * 判断指定元素在当前容器中的位置
     * 
     * @param item
     *            要查询下标的元素
     * @return 指定元素在当前容器中的位置，如果不存在返回-1
     */
    public abstract int indexOf(KyouItem item);

    /**
     * 从容器中删除位于指定下标的元素
     * 
     * @param index
     *            要删除的元素的下标
     * @return 被删除掉的元素，或为null
     */
    public abstract KyouItem remove(int index);

    /**
     * 从容器中删除指定名称的元素
     * 
     * @param name
     *            要删除的元素的名称
     * @return 被删除掉的元素，或为null
     */
    public abstract KyouItem remove(String name);

    /**
     * 从容器中删除指定的元素
     * 
     * @param item
     *            要删除的元素
     * @return 该元素碑是否存在于此容器中
     */
    public abstract boolean remove(KyouItem item);
}
