package CA1;

public class DisjointSetNode<T>
{
//    //initialize disjoint set
//    public DisjointSetNode<?> parent=null;
//    //intialize node
//    public T data;
//    //union find by size
//    public int size=1;
//
//
//    //node method
//    public DisjointSetNode(T data)
//    {
//        this.data=data;
//    }


    //Quick union of disjoint sets containing elements p and q (Version 2)
    public static void union(int[] a, int p, int q) {
        a[find(a, q)] = find(a, p); //The root of q is made reference the root of p
    }

    //Iterative version of find - negative value stored at root
    public static int find(int[] a, int id) {
        if (a[id] == -1) return -1;
        while (a[id] != id) id = a[id];
        return id;
    }

//    public DisjointSetNode<?> getParent() {
//        return parent;
//    }
//
//    public void setParent(DisjointSetNode<?> parent) {
//        this.parent = parent;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public int getSize() {
//        return size;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//    }
}
