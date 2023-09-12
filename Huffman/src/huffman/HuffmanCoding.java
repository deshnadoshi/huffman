package huffman;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
 
/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 *
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;
 
    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) {
        fileName = f;
    }
 
    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
 
        int [] char_count = new int [128];
       
        // Sets the count of all of the characters to 0
        for (int i = 0; i < char_count.length; i++){
            char_count[i] = 0;
        }
 
        int num_char_total = 0;
 
        while (StdIn.hasNextChar() == true){
            // Incrementing the count of character based on ASCII index
            char char_value_index = StdIn.readChar();
            char_count[char_value_index]++;
            num_char_total++;
        }
       
        sortedCharFreqList = new ArrayList<>();
 
        for (int i = 0; i < char_count.length; i++){
            if (char_count[i] != 0){
                CharFreq add_char = new CharFreq((char)i, (double)char_count[i]/num_char_total);
                sortedCharFreqList.add(add_char);
            }
        }
 
        if (sortedCharFreqList.size() == 1){
            char extra_char = ' ';
            if (sortedCharFreqList.get(0).getCharacter() == 127){
                extra_char = (char) 0;
            } else {
                extra_char = (char)(sortedCharFreqList.get(0).getCharacter() + 1);
            }
            CharFreq special_case_char = new CharFreq(extra_char, 0.0);
            sortedCharFreqList.add(special_case_char);
        }
 
        Collections.sort(sortedCharFreqList);
 
    }
 
    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
 
   
 
    public void makeTree() {
 
        Queue <CharFreq> source = new Queue<CharFreq>();
        Queue <TreeNode> target = new Queue<TreeNode>();
       
        TreeNode left = new TreeNode();
        TreeNode right = new TreeNode();  
 
        // Make all data values a tree node (no children), and add to the source tree
        for (int i = 0; i < sortedCharFreqList.size(); i++){

            char data_char = sortedCharFreqList.get(i).getCharacter(); 
            double data_prob = sortedCharFreqList.get(i).getProbOcc(); 

            CharFreq data = new CharFreq(data_char, data_prob);
            source.enqueue(data);
        }
       
        int size_target = 0;
        
        while (source.isEmpty() == false){
           
            double source_key = 0;
            double target_key = 0;
 
            boolean source_full = false;
            boolean target_full = false;
 
            boolean children_non_null = false;
 
            size_target = target.size();
           
            boolean first_encoded = false;
 
            if (source.isEmpty() == false){
                source_full = true;
                source_key = source.peek().getProbOcc();
            }
 
            if (target.isEmpty() == false){
                target_full = true;
                target_key = target.peek().getData().getProbOcc();
            }
 
            int count_num_encoded = 0;
 
            if (source_full == true && target_full == true && source != null && target != null){
                int source_less_target = 1;
                if (source_key <= target_key){
                    source_less_target = 0;
                } 
                // try with switch so you can just use default case for when its bigger in source

                switch (source_less_target){
                    
                    case 0: TreeNode source_node = new TreeNode(source.dequeue(), null, null);
                            left = source_node;
                            count_num_encoded++;
                            first_encoded = true;  
                            break;
                    default: first_encoded = true;
                            left = target.dequeue();
                            count_num_encoded++;
                            break;
                }
            } else if (source_full == true && target_full == false && source != null){
                // Target has nothing in it, need to dequeue the first one from source
                TreeNode source_node = new TreeNode(source.dequeue(), null, null);
                first_encoded = true;  
                left = source_node;  
                count_num_encoded++;
               
            } else if (target_full == true && source_full == false && target != null){
                first_encoded = true;
                // Source has nothing it in, need to dequeue the first one from target
                left = target.dequeue();
                count_num_encoded++;
            }
 
            if (left == null){
                first_encoded = false;
            }
 
            // UNFINISHED: still have to look for the next smallest to dequeue and to add into right child -- finished.
           
            if (source.isEmpty() == false){
                source_full = true;
                source_key = source.peek().getProbOcc();
            }
 
            if (source.isEmpty() == true){
                source_full = false;
            }
 
            if (target.isEmpty() == false){
                target_full = true;
                target_key = target.peek().getData().getProbOcc();
            }
           
            if (target.isEmpty() == true){
                target_full = false;
            }
 
            boolean second_encoded = false;
 
            if (source_full == true && target_full == true && source != null && target != null){
                int source_less_target = 0;
                if (source_key <= target_key){
                    source_less_target = 1;
                }
                switch (source_less_target){
                    case 1: TreeNode source_node = new TreeNode(source.dequeue(), null, null);
                            second_encoded = true;
                            right = source_node;
                            count_num_encoded++; 
                            break;
                    default: right = target.dequeue();
                            second_encoded = true;
                            count_num_encoded++; 
                            break; 
                }
            } else if (source_full == true && target_full == false && source != null){
                // Target has nothing in it, need to dequeue the first one from source
                TreeNode source_node = new TreeNode(source.dequeue(), null, null);
                right = source_node;
                count_num_encoded++; 
                second_encoded = true;
            } else if (target_full == true && source_full == false && target != null){
                second_encoded = true;
                right = target.dequeue();
                count_num_encoded++; 
            }
 
            if (right == null){
                second_encoded = false;
            }
 
            if (right != null && left != null){
                // using as confirmation that children are non-null, because value of second_encoded was messed up earlier
                children_non_null = true; // delete from condition later
            }

            boolean combine_allowed = true; 

            // System.out.println("count num encoded: " + count_num_encoded); 
            
            if (first_encoded != false && second_encoded != false && children_non_null== true && combine_allowed == true){
                // Adds together the probabilites for the new parent node
                CharFreq base_probability = new CharFreq(null, left.getData().getProbOcc() + right.getData().getProbOcc());
                TreeNode root = new TreeNode(base_probability, left, right);

                if (base_probability.getProbOcc() > 1){
                    combine_allowed = false; 
                } else {
                    combine_allowed = true;
                    target.enqueue(root);
                }
                
            }

            //PROBLEM: getting trees with 1.59, 1.24 probability 
                // need to avoid merging further if the combo probocc becomes more than 1 

            int left_done = 0; 
            int right_done = 0; 

            if (left == null || right == null && !(left == null && right == null)){
                if (left != null){
                    left_done++; 
                    if (right == null){
                        target.enqueue(left);
                    }
                } else if (right != null){
                    right_done++; 
                    if (left == null){
                        target.enqueue(right);
                    }
                }
            }

            // System.out.println(left_done + "left"); 
            // System.out.println(right_done + "right"); 

            // System.out.println("count num encoded: " + count_num_encoded); 

            
 
            // System.out.println("WHILE 1 -loop");
       
        }

        // need to 
        boolean combine_allowed_dup = true;

        while (target.size() != 1 && target != null){

            // System.out.println("WHILE 2 -loop");
            // PROBLEM: goes into infinite loop probably because of while condition -- CHANGE TO != 1 -- fixed. 
            // PROBLEM: null pointer 
                // probably issue with either the pointers to l/r being null or target being null
                // -- fixed.

            TreeNode left_copy = left; 
            TreeNode right_copy = right; 

            int left_done = 0; 
            int right_done = 0; 

            for (int i = 0; i < 2; i++){
                if (i == 0 && target != null && left_copy != null){
                    left_copy = target.dequeue(); 
                    left_done++; 
                } else if (i == 1 && target != null && right_copy != null){
                    right_copy = target.dequeue(); 
                    right_done++; 
                }
            }

            // System.out.println("right" + right_done); 
            // System.out.println("left" +left_done); 
            
            /* 

            !! NOT WORKING, IS ONLY ADDING SOME OF THE TARGET NODES!!
            for (int i = 0; i < 2; i++){
                if (left_done == 0){
                    left_copy = target.dequeue(); 
                } 

                if (right_done == 0 && left_done > 0){
                    right_copy = target.dequeue(); 
                }
            }
            */
            
            CharFreq base_probability; 
            TreeNode root;  

            if (left_done > 0 && right_done > 0){
                combine_allowed_dup = true; 
            }

            if (combine_allowed_dup == true){
                base_probability = new CharFreq(null, (left_copy.getData().getProbOcc() + right_copy.getData().getProbOcc()));
                root = new TreeNode(base_probability, left_copy, right_copy);
                // huffmanRoot.enqueue(root); 
                target.enqueue(root);
            }
            
            
        }
        
        huffmanRoot = target.dequeue();
 
    }
 
    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
   
    private void encoding(String [] encode, TreeNode huffman_copy, String encoded){
       
        char root_char_ind;
        String tree_path = "";
 
        /*  PREORDER (from Slides):
        private void preorder(Node x, Queue<Key> q) {
            if (x == null) return;
            q.enqueue(x.key);
            preorder(x.left, q); --> need to move left or right until it hits null (no children left)
            preorder(x.right, q); }
       
        */
 
        // PROBLEM: still only encoding b (10) -- FIXED.
        // Base Case: same as iterative base case (keep going until you hit null children)
 
        // PROBLEM: Null Pointer Excep. when checking if characters are null -- change if-condition -- FIXED.
 
        // huffman_copy.getData().getCharacter() == null &&
       
        // huffman_copy.getData().getCharacter() == null &&
 
        boolean left_non_null = true;
        boolean right_non_null = true;
 
        if (huffman_copy.getRight() != null){
            right_non_null = true;
        }

        // System.out.println(right_non_null + " 1 " + left_non_null); 
 
        if (huffman_copy.getLeft() != null){
            left_non_null = true;
            // System.out.println("zeros");
            tree_path = "0";
            encoding(encode, huffman_copy.getLeft(), encoded + tree_path);
        }

        // System.out.println(right_non_null + " 2 " + left_non_null); 
 
        if (huffman_copy.getLeft() != null){
            left_non_null = true;
        } else {
            left_non_null = false;
        }

        // System.out.println(right_non_null + " 3 " + left_non_null); 
 
        if (huffman_copy.getRight() != null){
            // System.out.println("ones");
            right_non_null = true;
            tree_path = "1";
            // PROBLEM: method fails (only a-> shows up) -- move increments to recursion
            // huffman_copy = huffman_copy.getRight();
            encoding(encode, huffman_copy.getRight(), encoded + tree_path);
        }
 
        // System.out.println(right_non_null + " 4 " + left_non_null);  

        // If the root has no children + character isn't null
        // add the string to the array
 
        if (huffman_copy.getLeft() == null && huffman_copy.getRight() == null && huffman_copy.getData().getCharacter() != null){
            root_char_ind = huffman_copy.getData().getCharacter();                
            encode [(int) root_char_ind] = encoded;
 
            // System.out.println(encoded + " - encoded");
            return;
        }
        // PROBLEM: Extra '1' for makeEncodings,
            //it's check if the corners aren't null and then adding the number anyway, even if it isn't in the path
 
            // PROBLEM: Removes too many bits from 'c's encoding -- fixed.
 
    }
 
 
     public void makeEncodings() {
 
        // PROBLEM: TA said iteratively requires more code, implement recursively --> PreOrder from slides
        String [] encode = new String [128];
        String encode_num = "";
        char root_char_ind = ' ';
 
        encoding(encode, huffmanRoot, encode_num);
        encodings = encode;

        int num_encoded_total = 0; 
        for (int i = 0; i < encode.length; i++){
            if (encode[i] != null){
                num_encoded_total++; 
            }
        }

        // System.out.println("ne: " + num_encoded_total); 
       
        /*  
        // goes through the Huffman tree until it reaches a character that has null as it's value
        while (huffman_copy.getLeft().getData().getCharacter() == null && huffman_copy.getRight().getData().getCharacter() == null){
            // PROBLEM: not entering the loop - FIXED.
            // PROBLEM: not adding anything to encoded --> all entries are null - FIXED.
 
            // PROBLEM: keeps adding only 111 (c) to the array, none of the other values
                     // keeps adding only 10 (b) to the array, infinite loop
                     // keeps adding 10 to the array, only runs through encoding once, then Null Pointer Exception
            if (huffman_copy.getRight() != null){
                System.out.println("ones");
                encode_num += "1";
                huffman_copy = huffman_copy.getRight();
            }
           
            if (huffman_copy.getLeft() != null){
                System.out.println("zeros");
                encode_num += "0";  
                huffman_copy = huffman_copy.getLeft();
            }
 
            if (huffman_copy.getRight() == null && huffman_copy.getLeft() == null){
                System.out.println("ADDING");
                root_char_ind = huffman_copy.getData().getCharacter();                
                encode [(int) root_char_ind] = encode_num;
                huffman_copy
               
            }
           
        }
 
        System.out.println("VALUE: " + encode_num);
 
        for (int i = 0; i < encode.length; i++){
            System.out.println(encode[i]);
        }
 
         
        for (int i = 0; i < encode.length; i++){
            if (encode[i] != null){
                num_encoded++;
            }
        }
 
        System.out.println("num encoded: " + num_encoded);
        */
 
    }
 
 
 
    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     *
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
 
        String final_encoded = "";
 
        while (StdIn.hasNextChar() == true){
            int ind_char = (int) StdIn.readChar();
            final_encoded += encodings[ind_char];
        }
 
        writeBitString(encodedFile, final_encoded);
 
    }
   
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     *
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;
 
        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;
 
        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }
 
            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
           
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
       
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }
 
    /**
     * Using a given encoded file name, this method makes use of the readBitString method
     * to convert the file into a bit string, then decodes the bit string using the
     * tree, and writes it to a decoded file.
     *
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
       
        String encoded_file = "";
        encoded_file = readBitString(encodedFile);
       
        char [] char_bits = new char [encoded_file.length()];
 
        for (int i = 0; i < char_bits.length; i++){
            char_bits[i] = encoded_file.charAt(i);
            // System.out.println(char_bits[i]);
        }
 
        int num_letters_encoded = 0;
       
        for (int i = 0; i < encodings.length; i++){
            if (encodings[i] != null){
                // System.out.println((char)i + ": " + encodings[i]);
                // PROBLEM: Goes into infinite loop when char with probability 0 is encountered -- FIXED.
 
                char check_0 = ' ';
 
                for (int j = 0; j < sortedCharFreqList.size(); j++){
                    if (sortedCharFreqList.get(j).getProbOcc() == 0.0){
                        check_0 = sortedCharFreqList.get(j).getCharacter();
                    }
                }
 
                if (check_0 != (char)i){
                    num_letters_encoded++;
                }
 
               
            }
        }
 
        // boolean all_encoded = false;
        int num_encoded = 0;
        TreeNode huffman_copy = huffmanRoot;
 
        while (num_encoded < num_letters_encoded){
 
            for (int i = 0; i < char_bits.length; i++){
                if (char_bits[i] == '0'){
                    if (huffman_copy != null){
                        huffman_copy = huffman_copy.getLeft();
                        if (huffman_copy.getData().getCharacter() != null){
                            char_bits[i] = 'x';
                            StdOut.print(huffman_copy.getData().getCharacter());
                            huffman_copy = huffmanRoot;
                            num_encoded++;
                        }
               
                    }
                   
                }
               
                if (char_bits[i] == '1') {
                    if (huffman_copy != null){
                        huffman_copy = huffman_copy.getRight();
                        if (huffman_copy.getData().getCharacter() != null){
                            char_bits[i] = 'x';
                            StdOut.print(huffman_copy.getData().getCharacter());
                            // Start from the top again
                            huffman_copy = huffmanRoot;
                            num_encoded++;
                        }
 
                       
                    }
                   
                }
               
 
            }
 
            /*
            for (int  i = 0; i < char_bits.length; i++){
                System.out.println(char_bits[i]);
               
            }
 
            */
 
            // Checks if all of the numbers from the array have been decoded (or set to empty character)
            // LOGIC:
            // To check if all are encoded - all characters must be x
            // If all are x, then there are no number chars
            // If you find even one non-x char, then all encoded = false
 
            /*
            for (int i = 0; i < char_bits.length; i++){
                if (char_bits[i] != 'x'){
                    all_encoded = false;  
                }
 
                System.out.println(all_encoded);
            }
            */
 
        }
 
    }
 
    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     *
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
       
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);
 
            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
           
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString +
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }
 
            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
           
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }
 
    /*
     * Getters used by the driver.
     * DO NOT EDIT or REMOVE
     */
 
    public String getFileName() {
        return fileName;
    }
 
    public ArrayList<CharFreq> getSortedCharFreqList() {
        return sortedCharFreqList;
    }
 
    public TreeNode getHuffmanRoot() {
        return huffmanRoot;
    }
 
    public String[] getEncodings() {
        return encodings;
    }
}
 

