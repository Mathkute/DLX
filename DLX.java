/*
 *
 */

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/* To change how it is initialised:
   DLX(String[] headersLabels): to store the headers' labels;
   addRow(int[] row): to add a row

   to change how it stops: to set a java.util.function.Predicate<T>:
   one needs to create an instance that has access to the reducedMap
   and copies it to do the checks
 */
/* test0: 2x2 box; shapes: 'L','.'
   **  *.  **  .* S0 = '.', S1 = 'L'
   *.  **  .*  **
   Matrix: S T 0 1 2 3
	   1 0 1 0 0 0
           1 0 0 1 0 0
           1 0 0 0 1 0
           1 0 0 0 0 1
           0 1 0 1 1 1
           0 1 1 0 1 1
           0 1 1 1 0 1
           0 1 1 1 1 0
*/

public class DLX {
    class Data {
        String id;
        Data U, D, L, R; Header C;
        Data(){this.U = this; this.D = this; this.L = this; this.R = this;}
    } // class Data //

    class Header extends Data {
        int S;
        String N;
        Header(){this.S = -1; this.N = "h";}
        Header(int S, String N){ this.S = S; this.N = N;}
    } // class Header //

    int nshapes, nx, ny;
    Header h;
    Header[] headers;
    Data[] O;
    List<String[]> solutions = new ArrayList<>();
 
    DLX(int nshapes, int nx, int ny, String[] names, List<int[]> rows) {
        this.nshapes = nshapes;
        this.nx = nx; this.ny = ny;
        addHeaders(names);
        Iterator<int[]> ir = rows.iterator();
        while(ir.hasNext()) {addRow(ir.next());}
    }// DLX() //

    void addHeaders(String[] headerLabels){
        this.h = new Header();
        this.headers = new Header[headerLabels.length+1];
        this.headers[0] = this.h;
        this.O = new Data[headerLabels.length];
        for(int i=1; i<this.headers.length; i++) {
            // System.err.print(headerLabels[i-1]+ " ");
            this.headers[i] = new Header(0,headerLabels[i-1]);
            this.headers[i].C = this.headers[i];
            this.headers[i-1].R = this.headers[i];
            this.headers[i].id = "H "+i;
        }
        this.headers[this.headers.length-1].R = this.headers[0];
        // System.err.println();

        for(int i=0; i<this.headers.length; i++) {
            this.headers[i].L = this.headers[((i-1+this.headers.length)%this.headers.length)];
            this.headers[i].U = this.headers[i];
            this.headers[i].D = this.headers[i];
        }
    } // addHeaders(String[]) //

    void addRow(int[] row) {
        Data first = new Data();
        Data last = first;
        for(int x=0; x<row.length; x++) {
            int i = row[x];
            Data one = new Data();
            this.headers[i+1].S++;
            one.C = this.headers[i+1];
            one.D = this.headers[i+1];
            this.headers[i+1].U.D = one;
            one.U = this.headers[i+1].U;
            this.headers[i+1].U = one;
            last.R = one; one.L = last;
            first.L = one; one.R = first;
            last = one;
        }
        last.R = first.R;
        first.R.L = last;
    } /* addRow() */

    boolean stop = false;
    void search(int k){
        if(stop) return; 
        if(h.R == h) { stop=storeSolution(); return;}
        Data c = h.R;

        cover(c);
        Data r = c;
        while((r=r.D) != c){ 
            O[k] = r;
            Data j = r;
            while((j=j.R) != r){ cover(j.C);}
            search(k+1);
            r = O[k];
            j = r;
            while((j=j.L)!= r){ uncover(j.C);}
        }
        uncover(c);
    } // search(int) //

    void cover(Data c){
        c.L.R = c.R; 
        c.R.L = c.L; 
        Data i = c;  
        while( (i = i.D) != c){ 
            Data k = i;
            while((k=k.R) != i){
                k.D.U = k.U;
                k.U.D = k.D;
                k.C.S = k.C.S - 1; 
            }
        }
    } // cover() //

    void uncover(Data c){
        Data i = c;
        while((i = i.U) != c){
            Data j = i;
            while((j = j.L) != i){
                j.C.S = j.C.S + 1;
                j.D.U = j;
                j.U.D = j;
            }
        }
        c.R.L = c;
        c.L.R = c;
    } // uncover() //

    boolean storeSolution(){
        // System.err.print("storeSolution(): ");
        List<String> ls = new ArrayList<>();
        for(int i=0; i<this.nshapes;i++){
            Data o = this.O[i];
            Data c = o;
            String n = "";
            do{ 
                n += c.C.N + (c.R!=o?" ":"");
            } while((c = c.R) != o);
            ls.add(n);
            // System.err.print(n+" ");
        }
        solutions.add(ls.toArray(new String[0]));
        return false;
    } // storeSolution() //

    List<String[]> getSolutions(){return solutions;} // getSolutions() //

    void pMatrix(){
        List<String> matrix = new ArrayList<>();
        int i=0;
        Data head = this.h;
        while((head = head.R) != this.h && i<this.nshapes){
            Data r = head;
            while( (r=r.D) != head) {
                String row = "";
                Data c = r;
                do{
                    row += c.C.N + " ";
                }while((c = c.R) != r);
                matrix.add(row);
            }
            i++;
        }
        
        Iterator<String> im = matrix.iterator();
        while(im.hasNext()){
            char[] r = new char[this.nshapes+this.nx*this.ny];
            for(int j=0; j<r.length; j++) r[j] = '0';
            String[] rs = im.next().split("\\s");
            int j=1, k=0;
            while(j<this.headers.length && k <rs.length){
                while(j<this.headers.length
                   && k <rs.length
                   && !this.headers[j].N.equals(rs[k])) j++;
                r[j-1] = '1';
                j++; k++;
            }
            System.out.println(new String(r));
        }
    } // pMatrix() //

    void pSolutions(){
        int count = 0;
        Iterator<String[]> isol = this.solutions.iterator();
        while(isol.hasNext()) {
            char[] out = new char[nx*ny];
            String[] ta = isol.next();
            for(String t: ta){
                // System.err.print("pSolutions(): "+t);
                Scanner ts = new Scanner(t);
                String smb = ts.next();
                char symbol = smb.charAt(0);
                while(ts.hasNext()) {
                    int i=ts.nextInt();
                    out[i] = symbol;
                }
            }
            count++;
            String o = new String(out);
            System.out.println(count);
            for(int i=0; i<ny; i++)
                System.out.println(o.substring(i*nx,(i+1)*nx));
        }
    } // pSolutions() //

} // class DLX //

class TestDLX {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        int nshapes = in.nextInt();
        int nrows   = in.nextInt();
        int nx = in.nextInt();
        int ny = in.nextInt();
        if(in.hasNextLine()) in.nextLine();
        
        String[] names = in.nextLine().split("\\s");
        // String ln = "";
        // for(String s: names) ln += s;
        // System.err.println(ln);
        List<int[]> rows = new ArrayList<>();

        while(in.hasNextLine()){
            // String ns = "";
            String[] line = in.nextLine().split("\\s");
            List<Integer> li = new ArrayList<>();
            for(int x=0; x<line.length; x++){
                // ns += Integer.parseInt(line[x])+" ";
                if(Integer.parseInt(line[x]) == 1) li.add(x);
            }
            // System.err.println(ns);
            int x = 0;
            int[] ai = new int[li.size()];
            Iterator<Integer> ili = li.iterator();
            while(ili.hasNext()) {ai[x] = ili.next(); x++;}
            rows.add(ai);
            // System.err.println(ai.length);
        }

        DLX d = new DLX(nshapes, nx, ny, names, rows);
        d.search(0);
        // d.pSolutions();
    }
} // class TestDLX //

/*
 *
 */
