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

   to change how it stops: to use a
   method that checks the solution
   and returns a boolean.
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
    public static void main(String[] args){
        DLX d = new DLX();
        d.search(0);
        d.pSolutions();
    }

    class Data {
        String id;
        Data U, D, L, R; Header C;
        Data(){this.U = this; this.D = this; this.L = this; this.R = this;}
    }

    class Header extends Data {
        int S;
        String N;
        Header(){this.S = -1; this.N = "h";}
        Header(int S, String N){ this.S = S; this.N = N;}
    } /* class Header */

    DLX() {
        this.nshapes = in.nextInt();
        this.nrows   = in.nextInt();
        this.nx = in.nextInt();
        this.ny = in.nextInt();
        if(in.hasNextLine()) in.nextLine();

        String[] names = in.nextLine().split("\\s");
        addHeaders(names);

        int rowl = this.nshapes+this.nx*this.ny;
        for(int y=0; y<this.nrows; y++) {
            List<Integer> li = new ArrayList<>();
            for(int x=0; x< rowl; x++){
                if(in.nextInt() == 1) li.add(x);
            }
            int x = 0;
            int[] ai = new int[li.size()];
            Iterator<Integer> ili = li.iterator();
            while(ili.hasNext()) {ai[x] = ili.next(); x++;}
            // System.err.println(ai.length);
            addRow(ai);
            if(in.hasNextLine()) in.nextLine();
        }
        // pMatrix();
    }// DLX() //

    Scanner in = new Scanner(System.in);
    int nrows, nshapes, nx, ny;
    /*
    DLX(){
        this.nshapes = in.nextInt();
        this.nrows   = in.nextInt();
        this.nx = in.nextInt();
        this.ny = in.nextInt();
        if(in.hasNextLine()) in.nextLine();

        String[] names = in.nextLine().split("\\s");
        if(names.length != (this.nshapes+this.nx*this.ny))
            System.err.println("Error: number of names does not match.");
        this.O = new Data[names.length];
        this.headers = new Header[names.length+1];
        this.h = new Header();
        this.headers[0] = this.h;
        for(int i=1; i<this.headers.length; i++) {
            this.headers[i] = new Header(0,names[i-1]);
            this.headers[i].C = this.headers[i];
            this.headers[i-1].R = this.headers[i];
            this.headers[i].id = "H "+i;
        }
        this.headers[this.headers.length-1].R = this.headers[0];

        for(int i=0; i<this.headers.length; i++) {
            this.headers[i].L = this.headers[((i-1+this.headers.length)%this.headers.length)];
            this.headers[i].U = this.headers[i];
            this.headers[i].D = this.headers[i];
        }
        
        for(int j=0; j<this.nrows; j++) {
            Data first = new Data();
            Data last = first;
            for(int i=0; i<this.headers.length-1; i++) {
                if(in.nextInt()==1) {
                    Data one = new Data();
                    one.id = "" + j + " " + i;
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
            }
            last.R = first.R;
            first.R.L = last;
            if(in.hasNextLine()) in.nextLine();
        }
    } // constructor DLX() //
    */

    void addHeaders(String[] headerLabels){
        this.h = new Header();
        this.headers = new Header[headerLabels.length+1];
        this.headers[0] = this.h;
        this.O = new Data[headerLabels.length];
        for(int i=1; i<this.headers.length; i++) {
            this.headers[i] = new Header(0,headerLabels[i-1]);
            this.headers[i].C = this.headers[i];
            this.headers[i-1].R = this.headers[i];
            this.headers[i].id = "H "+i;
        }
        this.headers[this.headers.length-1].R = this.headers[0];

        for(int i=0; i<this.headers.length; i++) {
            this.headers[i].L = this.headers[((i-1+this.headers.length)%this.headers.length)];
            this.headers[i].U = this.headers[i];
            this.headers[i].D = this.headers[i];
        }
    } /* DLX(String[]) */

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

    Set<String[]> solutions = new HashSet<>();
    Header h;
    Header[] headers;
    Data[] O;
    void search(int k){
        //TODO: to change storeSolution(): to return a boolean:
        //      stop = storeSolution();
        //      if(stop) return; 
        if(h.R == h) { storeSolution(); return;}
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
    }

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
    } /* cover() */

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
    } /* uncover() */

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
    } /* pMatrix() */

    void storeSolution(){
        String n = "";
        List<String> ls = new ArrayList<>();
        for(int i=0; i<this.nshapes;i++){
            Data o = this.O[i];
            Data c = o;
            n = "";
            do{ 
                n += c.C.N + (/*c.R!=null &&*/ c.R!=o?" ":"");
            } while((c = c.R) /*!= null && c  */!= o);
            ls.add(n);
        }
        solutions.add(ls.toArray(new String[0]));
    } // storeSolution() //

    void pSolutions(){
        int count = 0;
        Iterator<String[]> isol = this.solutions.iterator();
        while(isol.hasNext()) {
            char[] out = new char[this.nx*this.ny];
            String[] ta = isol.next();
            for(String t: ta){
                // System.err.println("pSolutions(): "+t);
                Scanner ts = new Scanner(t);
                String smb = ts.next();
                char symbol = smb.charAt(0);
                while(ts.hasNext()) {
                    int i=ts.nextInt();
                    out[i] = symbol;
                    //System.err.print(i);
                }
            }
            count++;
            String o = new String(out);
            System.out.println(count);
            for(int i=0; i<this.ny; i++)
                System.out.println(o.substring(i*this.nx,(i+1)*this.nx));
        }
    } // pSolutions() //

} // class DLX //

/*
 *
 */
