import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
     * Fisierul de iesire.
     **/
    protected String outputFile;

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
     * @param outputFile numele fisierului de iesire
     */
    public SortFilter(String sName, BufferedReader pInput, BufferedWriter pOutput, String outputFile) {
        // Executarea constructorului clasei parinte.
        super(sName);

        // Initializarea porturilor de intrare si de iesire.
        this.pInput = pInput;
        this.pOutput = pOutput;
        this.outputFile = outputFile;
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
        Student objStudent = new Student(pInput.readLine());
        students.add(objStudent);

        // Sorteaza lista curenta dupa nume
        students.sort(Comparator.comparing(student -> student.sName));

        // Rescrie fisierul la fiecare student nou
        pOutput.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Student s : students) {
            writer.write(s.toString());
            writer.newLine();
        }
        writer.flush();
        pOutput = writer;
    }
}
