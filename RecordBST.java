//Jason Lomsdalen
//Assignment 4
//Buckalew
import java.util.*;

public class RecordBST 
{
	public Node root;

    public RecordBST()
    {
        root = null;
    }

    //Insert Comparable into BST based off of compareTo method
	public void insert(Comparable x) 
    {
        //Assign Node to root for first element in BST
        if (root == null) 
        {
            root = new Node(x);
        }
        //Sort in rest of items based on compareTo method
        else
        {
            Node p = root;
            while (p!=null) 
            {
                if (p.item.compareTo(x) < 0) 
                {
                    if(p.right == null)
                    {
                        p.right = new Node(x);
                        break;
                    }
                    else
                    {
                        p = p.right;
                    }
                }
                else
                {
                    if(p.left == null)
                    {
                        p.left = new Node(x);
                        break;
                    }
                    else
                    {
                        p = p.left;
                    }
                }
            }
        }
        return;
    }

    //Searches BST for dictionary word
    public boolean searchSpelling(Comparable x)
    {
    	Node p = root;
    	while(p != null)
    	{
    		if(p.item.compareTo(x) == 0)
    		{
    			return true;
    		}
    		else if (p.item.compareTo(x) < 0)
    		{
    			p = p.right;
    		}
    		else
    		{
    			p = p.left;
    		}
    	}
    	return false;
    }

    //Searches BST for alarm word
    public void searchAlarm(Comparable x)
    {
        Node p = root;
        while(p != null)
        {
            if(p.item.compareTo(x) == 0)
            {
                ItemRecord check = (ItemRecord)p.item;
                check.setTrue();
                return;
            }
            else if (p.item.compareTo(x) < 0)
            {
                p = p.right;
            }
            else
            {
                p = p.left;
            }
        }
        return;
    }

    //Prints alarms word in an in order traversal
    public void inOrder(Node x)
    {
        if(x != null)
        {
            inOrder(x.left);
            ItemRecord newItem = (ItemRecord)x.item;
            if(newItem.flag == true)
            {    
                System.out.println(newItem.token);
            }
            inOrder(x.right);
        }
    }

    private static class Node 
    {
        private Comparable item;
        private Node left;
        private Node right;

        private Node(Comparable comparable) 
        {
            this.item = comparable;
            this.left = null;
            this.right = null;
        }
    }
}