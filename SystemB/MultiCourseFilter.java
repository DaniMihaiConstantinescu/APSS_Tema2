import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * Filtru care verifica daca studentul a absolvit cel putin unul dintr-o lista de cursuri.
 * Daca da, inregistrarea este transferata la portul de iesire.
 */
public class MultiCourseFilter extends Filter {

    /**
     * Portul de intrare.
     **/
    protected BufferedReader pInput;

    /**
     * Portul de iesire.
     **/
    protected BufferedWriter pOutput;

    /**
     * Lista cursurilor.
     **/
    protected List<Integer> courses;

    /**
     * Construirea unui filtru pentru lista de cursuri cu un nume dat.
     * Porturile de intrare si de iesire sunt impachetate intr-un flux de caractere buffer-at.
     *
     * @param sName   sirul ce reprezinta numele filtrului
     * @param pInput  portul de intrare al acestui filtru
     * @param pOutput portul de iesire al acestui filtru
     * @param courses lista de cursuri pentru verificare
     */
    public MultiCourseFilter(String sName,
                             BufferedReader pInput, BufferedWriter pOutput,
                             List<Integer> courses) {
        super(sName);
        this.pInput = pInput;
        this.pOutput = pOutput;
        this.courses = courses;
    }

    /**
     * Indica disponibilitatea datelor pe portul de intrare.
     *
     * @return <code>true</code> daca si numai daca acest filtru poate citi date de la portul de intrare.
     * @throws IOException
     */
    @Override
    protected boolean ready() throws IOException {
        return this.pInput.ready();
    }

    /**
     * Citeste datele disponibile de la portul de intrare si le scrie la portul de iesire
     * daca studentul a absolvit cel putin unul dintre cursurile din lista.
     *
     * @throws IOException
     */
    @Override
    protected void work() throws IOException {
        String line = this.pInput.readLine();
        if (line == null) return;

        Student objStudent = new Student(line);

        // Verificam daca studentul a absolvit cel putin unul dintre cursuri
        boolean passedAny = false;
        for (int course : courses) {
            if (objStudent.hasCompleted(course)) {
                passedAny = true;
                break;
            }
        }

        if (passedAny) {
            this.pOutput.write(objStudent.toString());
            this.pOutput.newLine();
            this.pOutput.flush();
        }
    }
}
