import java.util.*;
import java.io.*;

public class homework6{
    static int variablevalue=0; 
    static HashSet<ArrayList<ArrayList<String>>> visited = new HashSet<ArrayList<ArrayList<String>>>();

    static ArrayList<String> getLiteral(String s,int count){
        ArrayList<String> literal=new ArrayList<String>();
        int j=0;
        int k=0;
        int flag=0;
        if(s.charAt(0)=='~'){
            j=1;k=1;flag=1;
        }else{
            j=0;k=0;flag=0;
        }
        while(s.charAt(j) != '(') {
            j++;
        }
        String s1=new String(s.substring(k,j));
        j++;
        k=j;
        literal.add(s1);
        if(flag==1)
            literal.add("false");
        else 
            literal.add("true");
        while(k<s.length()){
            while(s.charAt(j)!=',' && s.charAt(j)!=')'){
                j++;
            }
            s1=s.substring(k,j);
            if(Character.isLowerCase(s1.charAt(0))){
                s1=s1+count;
            }
            literal.add(s1);
            j++;
            k=j;
            //System.out.println(k+"   "+j);
        }
        //System.out.println(literal);
        return literal;
    }

    static HashSet<ArrayList<String>> getSentence(String s,int count){
        HashSet<ArrayList<String>> sentence=new HashSet<ArrayList<String>>();
        int k=0;
		int j=0;
		int flag=0;
        while(k<s.length()){
            while(s.charAt(j)!=')'){
                j++;
            }
            j++;
            String s1=new String(s.substring(k,j));
            if(s1.charAt(0)=='~' && flag==0){
                s1=s.substring(k+1,j);
            }
            else if(flag==0){
                s1='~'+s1;
			}
            //System.out.println(s1);
            ArrayList<String> literal=new ArrayList<String>();
            literal=getLiteral(s1,count);
            sentence.add(literal);
            if(j==s.length())break;
            while(!Character.isDigit(s.charAt(j)) && !Character.isLetter(s.charAt(j)) && s.charAt(j)!='~'){
                if(s.charAt(j)=='=') 
                    flag=1;
                j++;
            }
            k=j;
        }
        //System.out.println(sentence);
        return sentence;
    }
    
    static ArrayList<HashSet<ArrayList<String>>> knowledgeBase(ArrayList<String> kb){
		int count=0;
		ArrayList<HashSet<ArrayList<String>>> KB = new ArrayList<HashSet<ArrayList<String>>>();
        for(int i=0;i<kb.size();i++){
            if(kb.get(i).contains("=>")){
                HashSet<ArrayList<String>> sentence=new HashSet<ArrayList<String>>();
				sentence=getSentence(kb.get(i),count);
				count++;
                KB.add(sentence);
			}
			else if(kb.get(i).contains("&")){
				int k=0;
				int j=0;
				while(k<kb.get(i).length()){
					while(kb.get(i).charAt(j)!=')'){
						j++;
					}
					j++;
					String s1=new String(kb.get(i).substring(k,j));
					HashSet<ArrayList<String>> sentence=new HashSet<ArrayList<String>>();
					ArrayList<String> literal=new ArrayList<String>();
					literal=getLiteral(s1,count);
					count++;
					sentence.add(literal);
					KB.add(sentence);
					if(j==kb.get(i).length())
						break;
					while(!Character.isDigit(kb.get(i).charAt(j)) && !Character.isLetter(kb.get(i).charAt(j)) && kb.get(i).charAt(j)!='~'){
						j++;
					}
					k=j;
				}
			}
            else{
				//System.out.println(kb.get(i));
                HashSet<ArrayList<String>> sentence=new HashSet<ArrayList<String>>();
                ArrayList<String> literal=new ArrayList<String>();
				literal=getLiteral(kb.get(i),count);
				count++;
                //System.out.print(literal);
                sentence.add(literal);
                KB.add(sentence);
			}
			//System.out.println(KB);
        }
        return KB;
    }

    static Boolean presentInVisited(ArrayList<ArrayList<String>> query){
        for(Iterator<ArrayList<ArrayList<String>>> it=visited.iterator();it.hasNext();){
            ArrayList<ArrayList<String>> query2=new ArrayList<ArrayList<String>>();
            query2=it.next();
            if(query2.equals(query)){
                return true;
            }
        }
        return false;
    }

    //gives all the variable with their replacing values if unifiable, gives false if cannot be unified
    //returns an empty arraylist if both are totaly equal and nothing needs to be changed.
    static ArrayList<ArrayList<String>> get_unifyingValue(ArrayList<String> literal1, ArrayList<String> literal2){
        ArrayList<ArrayList<String>> values=new ArrayList<ArrayList<String>>();
        int flag=0;
        for(int i=2;i<literal1.size();i++){
            String s1=new String();
            String s2=new String();
            s1=literal1.get(i);
            s2=literal2.get(i);
            if((Character.isLowerCase(s1.charAt(0)) && Character.isUpperCase(s2.charAt(0)))){
                //System.out.println("");
                int found=0;
                for(int j=0;j<values.size();j++){
                    if(values.get(j).get(0).equals(s1)){
                        found=1;
                        if(Character.isUpperCase(values.get(j).get(1).charAt(0))){
                            if(!values.get(j).get(1).equals(s2)){
                                values.clear();
                                ArrayList<String> variable = new ArrayList<String>();
                                variable.add("false");
                                values.add(variable);
                                flag=1;
                            }
                        }
                        else{
                            ArrayList<String> variable = new ArrayList<String>();
                            variable.add(values.get(j).get(1));
                            variable.add(s2);
                            values.add(variable);
                            values.get(j).set(1,s2);
                            flag=1;
                        }
                    }
                }
                if(found==0){
                    ArrayList<String> variable = new ArrayList<String>();
                    variable.add(s1);
                    variable.add(s2);
                    values.add(variable);
                    //System.out.println(values);
                    flag=1;
                }
            }
            else if(Character.isLowerCase(s2.charAt(0)) && Character.isUpperCase(s1.charAt(0))){
                int found=0;
                for(int j=0;j<values.size();j++){
                    if(values.get(j).get(0).equals(s2)){
                        found=1;
                        if(Character.isUpperCase(values.get(j).get(1).charAt(0))){
                            if(!values.get(j).get(1).equals(s1)){
                                values.clear();
                                ArrayList<String> variable = new ArrayList<String>();
                                variable.add("false");
                                values.add(variable);
                                flag=1;
                                return values;
                            }
                        }
                        else{
                            ArrayList<String> variable = new ArrayList<String>();
                            variable.add(values.get(j).get(1));
                            variable.add(s1);
                            values.add(variable);
                            values.get(j).set(1,s1);
                            flag=1;
                        }
                    }
                }
                if(found==0){
                    ArrayList<String> variable = new ArrayList<String>();
                    variable.add(s2);
                    variable.add(s1);
                    values.add(variable);
                    //System.out.println(values);
                    flag=1;
                }
            }
            else if(Character.isUpperCase(s1.charAt(0)) && Character.isUpperCase(s2.charAt(0)) && !s1.equals(s2)){
                //System.out.println(s1+"******"+s2);
                values.clear();
                ArrayList<String> variable = new ArrayList<String>();
                variable.add("false");
                values.add(variable);
                flag=1;
                return values;
            }
            else if(Character.isLowerCase(s1.charAt(0)) && Character.isLowerCase(s2.charAt(0))){
                int pos_s1=-1;
                int pos_s2=-1;
                for(int j=0;j<values.size();j++){
                    if(values.get(j).get(0).equals(s1))
                        pos_s1=j;
                    else if(values.get(j).get(0).equals(s2))
                        pos_s2=j;
                }
                if(pos_s1==-1 && pos_s1==-1){
                    ArrayList<String> variable = new ArrayList<String>();
                    variable.add(s1);
                    variable.add(s2);
                    values.add(variable);
                    flag=1;
                }
                else if(pos_s1!=-1 && pos_s2==-1){
                    if(Character.isUpperCase(values.get(pos_s1).get(1).charAt(0))){
                        ArrayList<String> variable = new ArrayList<String>();
                        variable.add(s2);
                        variable.add(values.get(pos_s1).get(1));
                        values.add(variable);
                        flag=1;
                    }
                    else{
                        int f=0;
                        for(int k=0;k<values.size();k++){
                            if(values.get(pos_s1).get(1).equals(values.get(k).get(0))){
                                f=1;
                                ArrayList<String> variable = new ArrayList<String>();
                                variable.add(s2);
                                variable.add(values.get(k).get(1));
                                values.add(variable);
                                values.get(pos_s1).set(1,values.get(k).get(1));
                                flag=1;
                            }
                        }
                        if(f!=1){
                            ArrayList<String> variable = new ArrayList<String>();
                            variable.add(s2);
                            variable.add(values.get(pos_s1).get(1));
                            values.add(variable);
                            flag=1;
                        }
                    }
                }
                else if(pos_s2!=-1 && pos_s1==-1){
                    if(Character.isUpperCase(values.get(pos_s2).get(1).charAt(0))){
                        ArrayList<String> variable = new ArrayList<String>();
                        variable.add(s1);
                        variable.add(values.get(pos_s2).get(1));
                        values.add(variable);
                        flag=1;
                    }
                    else{
                        int f=0;
                        for(int k=0;k<values.size();k++){
                            if(values.get(pos_s2).get(1).equals(values.get(k).get(0))){
                                f=1;
                                ArrayList<String> variable = new ArrayList<String>();
                                variable.add(s1);
                                variable.add(values.get(k).get(1));
                                values.add(variable);
                                values.get(pos_s2).set(1,values.get(k).get(1));
                                flag=1;
                            }
                        }
                        if(f!=1){
                            ArrayList<String> variable = new ArrayList<String>();
                            variable.add(s1);
                            variable.add(values.get(pos_s2).get(1));
                            values.add(variable);
                            flag=1;
                        }
                    }
                }
            }
        }//end of for loop
        if(flag==0){
            ArrayList<String> variable = new ArrayList<String>();
            variable.add("true");
            values.add(variable);
        }
        //System.out.println(values);
        return values;
    }
    
    static Boolean resolve(HashSet<ArrayList<String>> query1, ArrayList<HashSet<ArrayList<String>>> KB){
        Queue<HashSet<ArrayList<String>>> queue = new LinkedList<HashSet<ArrayList<String>>>(); 
        queue.add(query1);
        //System.out.println("abhi queue hai" + queue);
        //starting the clock timer
        long start = System.currentTimeMillis();
        while(queue.size()!=0){
            //System.out.println(visited);
            //System.out.println("QUEUE IS________________"+queue);
            HashSet<ArrayList<String>> query=new HashSet<ArrayList<String>>();
            query=queue.remove();
            //System.out.println("query in while loop" + query);
            for(int temp=0;temp<KB.size();temp++){
                long end = System.currentTimeMillis();
                float sec = (end - start) / 1000F;
                if(sec>=7.00)
                    return false;
				HashSet<ArrayList<String>> sentence=new HashSet<ArrayList<String>>();
                sentence = KB.get(temp);
                //System.out.println(query+"     "+sentence);
                //check every literal of query with every literal of the sentence
                for(Iterator<ArrayList<String>> itq=query.iterator();itq.hasNext();){
                    ArrayList<String> query_literal=new ArrayList<String>();
                    query_literal=itq.next();
                    for(Iterator<ArrayList<String>> its=sentence.iterator();its.hasNext();){
                        ArrayList<String> sentence_literal=new ArrayList<String>();
                        sentence_literal=its.next();

                        if(query_literal.get(0).equals(sentence_literal.get(0))){
                        //check if both are of opposite signs
                            if((query_literal.get(1).equals("true") && sentence_literal.get(1).equals("false")) || (query_literal.get(1).equals("false") && sentence_literal.get(1).equals("true"))){                
                            //get unifying values for the sentence
                                //System.out.println(query+" ------> "+sentence);
                                ArrayList<ArrayList<String>> unifyingValue=new ArrayList<ArrayList<String>>();
                                unifyingValue=get_unifyingValue(query_literal,sentence_literal);
                                //System.out.println("Unifying values are====>>"+unifyingValue);
                                if(unifyingValue.get(0).get(0)!="false"){
                                    //making copy of the sentence and the query
                                    HashSet<ArrayList<String>> sentencecopy=new HashSet<ArrayList<String>>();
                                    for(Iterator<ArrayList<String>> it=sentence.iterator();it.hasNext();){
                                        ArrayList<String> literal=new ArrayList<String>();
                                        ArrayList<String> literalcopy=new ArrayList<String>();
                                        literal=it.next();
                                        for(int k=0;k<literal.size();k++){
                                            String x=new String(literal.get(k));
                                            literalcopy.add(x);
                                        }
                                        sentencecopy.add(literalcopy);
                                    }

                                    HashSet<ArrayList<String>> querycopy=new HashSet<ArrayList<String>>();
                                    for(Iterator<ArrayList<String>> it=query.iterator();it.hasNext();){
                                        ArrayList<String> literal=new ArrayList<String>();
                                        ArrayList<String> literalcopy=new ArrayList<String>();
                                        literal=it.next();
                                        for(int k=0;k<literal.size();k++){
                                            String x=new String(literal.get(k));
                                            literalcopy.add(x);
                                        }
                                        querycopy.add(literalcopy);
									}
									
                                    if(unifyingValue.get(0).get(0)!="true"){
                                        for(Iterator<ArrayList<String>> itqc=querycopy.iterator();itqc.hasNext();){
                                            ArrayList<String> qc_literal=new ArrayList<String>();
                                            qc_literal=itqc.next();
                                            if(!qc_literal.equals(query_literal)){
                                            for(int x=0;x<unifyingValue.size();x++){
                                                for(int y=2;y<qc_literal.size();y++){
                                                    //System.out.println(qc_literal.get(y)+"         "+unifyingValue.get(x).get(0));
                                                    if(unifyingValue.get(x).get(0).equals(qc_literal.get(y))){
                                                        //if(!(Character.isLowerCase(unifyingValue.get(x).get(1).charAt(0))))
                                                        qc_literal.set(y,unifyingValue.get(x).get(1));
                                                    }
                                                }
                                            }
                                            }
                                        }
										querycopy.remove(query_literal);
										
                                        for(Iterator<ArrayList<String>> itsc=sentencecopy.iterator();itsc.hasNext();){
                                            ArrayList<String> sc_literal=new ArrayList<String>();
											sc_literal=itsc.next();
											if(!sentence_literal.equals(sc_literal)){
                                            for(int x=0;x<unifyingValue.size();x++){
                                                //System.out.println(unifyingValue.get(x).get(1).length());
                                                //if(Character.isLowerCase(unifyingValue.get(x).get(1).charAt(0))){
                                                    for(int y=2;y<sc_literal.size();y++){
                                                        if(unifyingValue.get(x).get(0).equals(sc_literal.get(y))){
                                                            //System.out.println(sentencecopy.get(k).get(y));  
                                                            sc_literal.set(y,unifyingValue.get(x).get(1));
                                                        }
                                                    }
                                                //}
                                            }
                                            querycopy.add(sc_literal);
                                            }
                                        }
                                    }
                                    else{
										querycopy.remove(query_literal);
										
										for(Iterator<ArrayList<String>> itsc=sentencecopy.iterator();itsc.hasNext();){
                                            ArrayList<String> sc_literal=new ArrayList<String>();
											sc_literal=itsc.next();
											if(!sentence_literal.equals(sc_literal)){
                                            	querycopy.add(sc_literal);
                                            }
                                        }
                                    }
                                    //System.out.println("***********"+querycopy);
                                    variablevalue++;
                                    for(Iterator<ArrayList<String>> itqc=querycopy.iterator();itqc.hasNext();){
                                        ArrayList<String> qc_literal=new ArrayList<String>();
                                        qc_literal=itqc.next();
                                        for(int y=2;y<qc_literal.size();y++){
                                            if(Character.isLowerCase(qc_literal.get(y).charAt(0))){
                                                qc_literal.set(y,qc_literal.get(y).substring(0,1) + variablevalue);
                                            }
                                        }
                                    }

                                    ArrayList<ArrayList<String>> temp_querycopy=new ArrayList<ArrayList<String>>();
                                    for(Iterator<ArrayList<String>> it=querycopy.iterator();it.hasNext();){
                                        ArrayList<String> literal=new ArrayList<String>();
                                        ArrayList<String> literalcopy=new ArrayList<String>();
                                        literal=it.next();
                                        for(int k=0;k<literal.size();k++){
                                            String x=new String(literal.get(k));
                                            if(Character.isLowerCase(x.charAt(0)) && k>1){
                                                x="x";
                                            }
                                            literalcopy.add(x);
                                        }
                                        temp_querycopy.add(literalcopy);
                                    }

                                    //System.out.println("********************************"+temp_querycopy);

                                    if(!presentInVisited(temp_querycopy)){
                                        visited.add(temp_querycopy);
                                        if(querycopy.size()!=0){
										    int flag=0;
										    for(int pos=0;pos<KB.size();pos++){
											    if(KB.get(pos).equals(querycopy))
												    flag=1;
										    }
										    if(flag==0){
                                                KB.add(querycopy);
                                                queue.add(querycopy);
                                            }
                                        }
                                        else{
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } 
        return false;
    }

    public static void main(String[] args){
        
        int number_of_queries=0;
        int number_of_sentences_in_kb=0;
        ArrayList<String> queries=new ArrayList<String>();
        ArrayList<String> kb=new ArrayList<String>();
        //reading the input from the text file and storing the queries and the kb in an ArrayList.
        try{
            File inputFile=new File("input.txt");
            Scanner myReader=new Scanner(inputFile);
            if(inputFile.canRead()){
                number_of_queries=myReader.nextInt();
                for(int i=0;i<number_of_queries;i++){
                    queries.add(myReader.next());
                }

                number_of_sentences_in_kb=myReader.nextInt();
                myReader.nextLine();
                for(int i=0;i<number_of_sentences_in_kb;i++){
                    String t=myReader.nextLine();
                    kb.add(t.trim());
                }
            }
            myReader.close();
        }catch (IOException e) {
            System.out.println("File Cannot be Read.");
            e.printStackTrace();
		}
		
        //Getting the Knowledge base in CNF form
        ArrayList<HashSet<ArrayList<String>>> KB=new ArrayList<HashSet<ArrayList<String>>>();
		KB=knowledgeBase(kb);
        //Creating an output file to write the output in it
        try{
            File outputFile=new File("output.txt");
            PrintWriter output = new PrintWriter(outputFile);

            //for Each query finding its truth value
            for(int i=0;i<number_of_queries;i++){
                String s=new String(queries.get(i));
                if(s.charAt(0)=='~'){
                    s=s.substring(1,s.length());
                }
                else{
                    s='~'+s;
                }
                ArrayList<String> q=new ArrayList<String>();
                variablevalue=KB.size();
                q=getLiteral(s,variablevalue);
                //do resolution and return whether the query is true or false
                HashSet<ArrayList<String>> query=new HashSet<ArrayList<String>>();
                query.add(q);
                //make a copy of the kb
                ArrayList<HashSet<ArrayList<String>>> KBcopy=new ArrayList<HashSet<ArrayList<String>>>();
                for(int temp=0;temp<KB.size();temp++){
                    HashSet<ArrayList<String>> sentence=new HashSet<ArrayList<String>>();
                    sentence = KB.get(temp);
                    HashSet<ArrayList<String>> sentencecopy=new HashSet<ArrayList<String>>();
                    for(Iterator<ArrayList<String>> it=sentence.iterator();it.hasNext();){
                        ArrayList<String> literal=new ArrayList<String>();
                        ArrayList<String> literalcopy=new ArrayList<String>();
                        literal=it.next();
                        for(int k=0;k<literal.size();k++){
                            String x=new String(literal.get(k));
                            literalcopy.add(x);
                        }
                        sentencecopy.add(literalcopy);
                    }
                    KBcopy.add(sentencecopy);
                }
                KBcopy.add(query);
                visited.clear();
				Boolean truth=resolve(query,KBcopy);
                if(truth==true)
                    output.println("TRUE");
                else
                    output.println("FALSE");
                System.out.println(truth);
            }
            output.close();
        }catch(IOException e){
        e.printStackTrace();
    }
    }
}