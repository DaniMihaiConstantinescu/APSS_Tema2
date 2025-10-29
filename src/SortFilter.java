import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Un filtru de sortare are un port de intrare si un port de iesire.
 * El citeste toate datele disponibile pe portul de intrare,
 * le sorteaza dupa nume si apoi le transmite la portul de iesire.
 */
public class SortFilter extends Filter {

    /**
     * Portul de intrare.
     **/
    protected BufferedReader pInput;

    /**
     * Portul de iesire.
     **/
    protected BufferedWriter pOutput;

    /**
     * Lista studentilor pastrati pentru sortare.
     **/
    private List<Student> students = new ArrayList<>();

    /**
     * Construieste un filtru de sortare cu numele dat.
     * Porturile sunt impachetate intr-un flux de caractere buffer-at.
     *
     * @param sName   sirul ce reprezinta numele filtrului
     * @param pInput  portul de intrare al acestui filtru
     * @param pOutput portul de iesire al acestui filtru
     */
    public SortFilter(String sName, BufferedReader pInput, BufferedWriter pOutput) {
        // Executarea constructorului clasei parinte.
        super(sName);

        // Initializarea porturilor de intrare si de iesire.
        this.pInput = pInput;
        this.pOutput = pOutput;
    }

    /**
     * Precizeaza daca datele sunt disponibile pe portul de intrare.
     *
     * @return <code>true</code> daca si numai daca acest filtru
     * poate citi date de la portul de intrare.
     * @throws IOException
     */
    @Override
    protected boolean ready() throws IOException {
        return this.pInput.ready();
    }

    /**
     * Citeste toate datele disponibile la portul de intrare,
     * le sorteaza dupa nume si le scrie la portul de iesire.
     *
     * @throws IOException
     */
    @Override
    protected void work() throws IOException {
        String line;

        // Citirea tuturor inregistrarilor din portul de intrare.
        while ((line = this.pInput.readLine()) != null) {
            Student objStudent = new Student(line);
            students.add(objStudent);
        }

        System.out.println("\n\n After read \n\n");

        // Sortarea listei de studenti dupa nume.
        students.sort(Comparator.comparing(student -> student.sName));

        System.out.println("\n\n After sort \n\n");

        // Scrierea inregistrarilor sortate la portul de iesire.
        for (Student s : students) {
            this.pOutput.write(s.toString());
            this.pOutput.newLine();
        }
        this.pOutput.flush();
    }
}
