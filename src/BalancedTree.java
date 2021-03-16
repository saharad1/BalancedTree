

class Node {
    Key key;
    Value v;
    int height;
    Node left;
    Node right;
    Node p;
    Value sumv;
    int size;

    Node(Key d,Value v) {
        this.key = d.createCopy();
        this.v=v.createCopy();
        height = 1;
        size=1;
        sumv=v.createCopy();
    }
}

class BalancedTree {

    Node root;

    // A utility function to get the height of the tree
    int height(Node N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // A utility function to get maximum of two integers
    int max(int a, int b) {
        return (a > b) ? a : b;
    }


    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        // my changes: assign parents to the node, update sizes
        x.right = y;
        y.p = x;
        y.left = T2;


        // Update heights
        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
        y.size = sizes(y.right) + sizes(y.left) + 1;
        x.size = sizes(x.right) + sizes(x.left) + 1;

        y.sumv = y.v.createCopy();
        if (y.left != null && y.left.sumv != null)
            y.sumv.addValue(y.left.sumv.createCopy());
        if (y.right != null && y.right.sumv != null)
            y.sumv.addValue(y.right.sumv.createCopy());
        x.sumv = x.v.createCopy();
        if (x.left != null && x.left.sumv != null)
            x.sumv.addValue(x.left.sumv.createCopy());
        if (x.right != null && x.right.sumv != null)
            x.sumv.addValue(x.right.sumv.createCopy());

        // Return new root in the "subtree" of the problematic nodes
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        // my changes: assigns parents to the node, update sizes
        y.left = x;

        x.right = T2;


        //  Update heights
        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;
        x.size = sizes(x.right) + sizes(x.left) + 1;
        y.size = sizes(y.right) + sizes(y.left) + 1;


        x.sumv = x.v.createCopy();
        if (x.left != null && x.left.sumv != null)
            x.sumv.addValue(x.left.sumv.createCopy());
        if (x.right != null && x.right.sumv != null)
            x.sumv.addValue(x.right.sumv.createCopy());
        y.sumv = y.v.createCopy();
        if (y.left != null && y.left.sumv != null)
            y.sumv.addValue(y.left.sumv.createCopy());
        if (y.right != null && y.right.sumv != null)
            y.sumv.addValue(y.right.sumv.createCopy());

        // Return new root
        return y;
    }

    // Get Balance factor of node N
    int getBalance(Node N) {
        if (N == null)
            return 0;

        return height(N.left) - height(N.right);
    }

    public void insert(Key newKey, Value newvalue) {
        Key keyToInsert = newKey.createCopy();
        Value valueToInsert = newvalue.createCopy();
        if (root == null) {
            this.root = insert(this.root, keyToInsert, valueToInsert);
            root.sumv = root.v.createCopy();
        } else {
            this.root = insert(this.root, keyToInsert, valueToInsert);



        }
    }

    private int sizes(Node x) {
        if (x == null)
            return 0;
        return x.size;
    }

    private Node insert(Node node, Key key, Value v) {

        /* 1.  Perform the normal BST insertion */
        if (node == null)
            return (new Node(key, v));

        if (key.compareTo(node.key) < 0)
            node.left = insert(node.left, key, v);
        else if (key.compareTo(node.key) > 0)
            node.right = insert(node.right, key, v);
        else // Duplicate keys not allowed
            return node;

        /* 2. Update height of this ancestor node */
        /* and updating my needs(size,sumv)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1*/
        node.height = 1 + max(height(node.left),
                height(node.right));
        node.size = 1 + sizes(node.left) + sizes(node.right);

        node.sumv = node.v.createCopy();
        if (node.left != null && node.left.sumv != null)
            node.sumv.addValue(node.left.sumv.createCopy());
        if (node.right != null && node.right.sumv != null)
            node.sumv.addValue(node.right.sumv.createCopy());

        /* 3. Get the balance factor of this ancestor
              node to check whether this node became
              unbalanced */
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there
        // are 4 cases Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0)
            return rightRotate(node);

        // Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0)
            return leftRotate(node);

        // Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        /* return the (unchanged) node pointer */
        return node;
    }

    Node minValueNode(Node node) {
        Node current = node;

        /* loop down to find the leftmost leaf */
        while (current.left != null)
            current = current.left;

        return current;
    }

    public void delete(Key key) {
        Key key_to_remove = key.createCopy();
        if (search(this.root, key_to_remove) == null)
            return;
        this.root = delete(this.root, key_to_remove);
    }

    private Node delete(Node node, Key key) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (node == null)
            return node;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key.compareTo(node.key) < 0)
            node.left = delete(node.left, key);

            // If the key to be deleted is greater than the
            // root's key, then it lies in right subtree
        else if (key.compareTo(node.key) > 0)
            node.right = delete(node.right, key);

            // if key is same as root's key, then this is the node
            // to be deleted
        else {
            // node with only one child or no child
            if ((node.left == null) || (node.right == null)) {
                Node temp = null;
                if (temp == node.left)
                    temp = node.right;
                else
                    temp = node.left;

                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                } else // One child case
                    node = temp; // Copy the contents of
                // the non-empty child
            } else {
                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                Node temp = minValueNode(node.right);

                // Copy the inorder successor's data to this node
                node.key = temp.key.createCopy();
                //new change!!!!!!!!!!!!!!!!!!
                node.v=temp.v.createCopy();

                // Delete the inorder successor
                node.right = delete(node.right, temp.key);
            }
        }

        // If the tree had only one node then return
        if (node == null)
            return node;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        node.height = max(height(node.left), height(node.right)) + 1;
        node.size = sizes(node.right) + sizes(node.left) + 1;

        node.sumv = node.v.createCopy();
        if (node.left != null && node.left.sumv != null)
            node.sumv.addValue(node.left.sumv.createCopy());
        if (node.right != null && node.right.sumv != null)
            node.sumv.addValue(node.right.sumv.createCopy());


        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public Value search(Key key) {
        Node x = search(this.root, key);
        if (x == null)
            return null;
        return x.v.createCopy();
    }

    private Node search(Node node, Key key) {
        if (node == null)
            return null;
        if (node.key.compareTo(key) == 0)
            return node;
        if (node.key.compareTo(key) > 0)
            return search(node.left, key);
        else
            return search(node.right, key);
    }


    public int rank(Key key){
        Node x= search(this.root,key);
        if(x==null)
            return 0;
        int r=1;
        x=this.root;
        while(x.key.compareTo(key)!=0){
            if(x.key.compareTo(key)<0){
                r+=sizes(x.left)+1;
                x=x.right;
            }
            else
                x=x.left;
        }
        r+=sizes(x.left);
        return r;
    }


    public Key select(int index) {
        if (root == null)
            return null;
        Node x = select(this.root, index);
        if (x == null)
            return null;
        return x.key.createCopy();
    }

    private Node select(Node x, int index) {
        if (index > sizes(x)|| x==null)
            return null;

        int l =0;
        if(x.left!=null)
            l=sizes(x.left);
        if (index == l + 1)
            return x;
        if (index <= l)
            return select(x.left, index);
        else
            return select(x.right, index - l - 1);
    }


    public Value sumValuesInInterval(Key key1, Key key2){

        if(this.root==null)
            return null;
        if(key1.compareTo(key2)==0){

            return search(key1);
        }
        if(key1.compareTo(key2)>0)
            return null;
        if(search(key1)==null){
            if(find_lowest_above_key(root,key1)!=null)
            key1=find_lowest_above_key(root,key1).createCopy();
            else
                key1=null;
        }
        if(search(key2)==null){
            if(find_highest_beneath_key(root,key2)!=null)
            key2=find_highest_beneath_key(root,key2).createCopy();
            else
                key2=null;
        }
        if(key1==null || key2==null)
            return null;

        Value v1=null;
        Value v2=null;
        Value val_to_ret_sum=null;
        Node ances=find_ancestor(root,key1,key2);//they are exist for sure
        if(ances.key.compareTo(key1)!=0)
            v1=aux_left(ances.left,key1.createCopy());
        if(ances.key.compareTo(key2)!=0)
        v2=aux_right(ances.right,key2.createCopy());

        val_to_ret_sum=ances.v.createCopy();
        Value v11;
        Value v22;
        if(v1!=null) {
            v11 = v1.createCopy();
            val_to_ret_sum.addValue(v11);
        }
        if(v2!=null) {
            v22=v2.createCopy();
            val_to_ret_sum.addValue(v22);
        }
        return val_to_ret_sum.createCopy();


    }
    private Value aux_left(Node x,Key k1){
        Value sub_sum=null;
        while (x!=null && x.key.compareTo(k1)!=0){
            if(x.key.compareTo(k1)<0){
                x=x.right;
            }
            else{
                if(sub_sum==null)
                    sub_sum=x.v.createCopy();
                else
                    sub_sum.addValue(x.v);
                if(x.right!=null)
                    sub_sum.addValue(x.right.sumv);
                x=x.left;
            }
            }
        if(x!=null && x.right!=null) {
            if(sub_sum!=null)
            sub_sum.addValue(x.right.sumv);
            else
                sub_sum=x.right.sumv.createCopy();
        }
            if (sub_sum != null && x!=null)
                sub_sum.addValue(x.v);
            else if(sub_sum==null&& x!=null)
                sub_sum = x.v.createCopy();



            if(sub_sum!=null)
                return sub_sum.createCopy();
            return null;

        }


     private Value aux_right(Node x,Key k2){
         Value sub_sum_to_ret=null;
         while(x!=null && x.key.compareTo(k2)!=0){
             if(x.key.compareTo(k2)<0){
                 if(sub_sum_to_ret==null)
                     sub_sum_to_ret=x.v.createCopy();
                 else
                     sub_sum_to_ret.addValue(x.v);
                 if(x.left!=null)
                     sub_sum_to_ret.addValue(x.left.sumv);
                 x=x.right;
             }
             else{
                 x=x.left;
             }

         }
         if(x!=null && x.left!=null){
             if(sub_sum_to_ret!=null)
                 sub_sum_to_ret.addValue(x.left.sumv);
             else
                 sub_sum_to_ret=x.left.sumv.createCopy();
         }

             if(sub_sum_to_ret!=null)
                 sub_sum_to_ret.addValue(x.v);
             else if(sub_sum_to_ret==null && x!=null)
                 sub_sum_to_ret=x.v.createCopy();

             if(sub_sum_to_ret!=null)
             return sub_sum_to_ret.createCopy();
         return null;
     }
    public Key find_lowest_above_key(Node x,Key key1){
        Key ret=null;
        if(key1==null)
            return null;
        while (x!=null){
            if(x.key.compareTo(key1)<0) {

                x = x.right;

            }
            else if(x.key.compareTo(key1)>0){
                ret=x.key.createCopy();
                x=x.left;

            }
            else{
                ret=x.key.createCopy();
                return ret;
            }

        }
        return ret;
    }
    public Key find_highest_beneath_key(Node x,Key key2){
        Key ret =null;
        while(x!=null){
            if(x.key.compareTo(key2)<0){
                ret = x.key.createCopy();
                x=x.right;
            }

            else if(x.key.compareTo(key2)>0){
                x=x.left;

            }
            else {
                ret=x.key.createCopy();
                return ret;
            }

        }
        return ret;
    }



    private Node find_ancestor(Node x,Key k1, Key k2){
        if (x!=null && x.key.compareTo(k1) >=0 && x.key.compareTo(k2)<=0)
            return x;
        if (x!=null && x.key.compareTo(k2)>0)
            return find_ancestor(x.left,k1,k2);
        else {
            if (x != null)
                return find_ancestor(x.right, k1, k2);
        }
        return null;
    }



}

