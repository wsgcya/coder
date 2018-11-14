package com.wsgcya.coder.common.util;


import java.util.*;
import java.util.function.Function;


/**
 * 集合框架工具类
 *
 * @author yewangwang@telincn.com
 * @date 2018-02-05 22:16
 */
public class CollectionUtil {
    /**
     * 创建<code>ArrayList</code>实例
     *
     * @param <E>
     *
     * @return <code>ArrayList</code>实例
     */
    public static <E> ArrayList<E> createArrayList() {
        return new ArrayList<E>();
    }

    /**
     * 创建<code>ArrayList</code>实例
     *
     * @param <E>
     * @param initialCapacity 初始化容量
     *
     * @return <code>ArrayList</code>实例
     */
    public static <E> ArrayList<E> createArrayList(int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }

    /**
     * 创建<code>LinkedList</code>实例
     *
     * @param <E>
     *
     * @return <code>LinkedList</code>实例
     */
    public static <E> LinkedList<E> createLinkedList() {
        return new LinkedList<E>();
    }


    /**
     * 判断<code>Collection</code>是否为<code>null</code>或空数组<code>[]</code>。
     *
     * @param collection
     *
     * @return 如果为空, 则返回<code>true</code>
     *
     * @see Collection
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || (collection.size() == 0);
    }

    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * 创建<code>HashMap</code>实例
     *
     * @param <K>
     * @param <V>
     * @param initialCapacity 初始化容量
     *
     * @return <code>HashMap</code>实例
     */
    public static <K, V> HashMap<K, V> createHashMap(int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }


    /**
     * 列表转换
     *
     * @param fromList 源列表
     * @param function 转换函数
     *
     * @return 新列表
     *
     * @see Function
     */
    public static <F, T> List<T> transform(Collection<F> fromList, Function<? super F, ? extends T> function) {
        if (CollectionUtil.isEmpty(fromList)) {
            return Collections.emptyList();
        }

        List<T> ret = new ArrayList<T>(fromList.size());
        for (F f : fromList) {
            T t = function.apply(f);
            if (t == null) {
                continue;
            }
            ret.add(t);
        }

        return ret;
    }

}
