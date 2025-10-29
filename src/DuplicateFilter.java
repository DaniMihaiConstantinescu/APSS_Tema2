import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Un filtru care duplica intrarile cu un port de intrare si doua porturi de iesire.
 * El transfera datele in portul de intrare pe cele doua porturi de iesire
 * fara a le modifica.
 */
public class DuplicateFilter extends Filter {

    /**
     * Portul de intrare.
     **/
    protected BufferedReader pInput;

    /**
     * Primul port de iesire.
     **/
    protected BufferedWriter pOutput1;

    /**
     * Al doilea port de iesire.
     **/
    protected BufferedWriter pOutput2;

    /**
     * Construieste un filtru pentru eliminarea unor coloane cu numele dat.
     * Porturile sunt impachetate intr-un flux de caratere buffer-at.
     *
     * @param sName   sirul ce reprezinta numele filtrului
     * @param pInput portul de intrare al acestui filtru
     * @param pOutput1 primul port de iesire al acestui filtru
     * @param pOutput2 al doilea port de iesire al acestui filtru
     */
    public DuplicateFilter(String sName,
                           BufferedReader pInput, BufferedWriter pOutput1, BufferedWriter pOutput2) {
        // Executarea constructorului clasei parinte.
        super(sName);

        // Initializarea porturilor de intrare si de iesire.
        this.pInput = pInput;
        this.pOutput1 = pOutput1;
        this.pOutput2 = pOutput2;
    }

    /**
     * Precizeaza daca datele sunt disponibile pe porturile de intrare.
     *
     * @return <code>true</code> daca si numai daca acest filtru
     * poate citi date de la unul din porturile de intrare.
     * @throws IOException
     */
    protected boolean ready() throws IOException {
        return this.pInput.ready();
    }

    /**
     * Citeste date disponibile la unul din porturile de intrare si
     * scrie date noi la portul de iesire.
     *
     * @throws IOException
     */
    protected void work() throws IOException {

        // Citirea unei inregistrari de pe portul de intrare selctat.
        Student objStudent = new Student(pInput.readLine());

        // Scrierea inregistrarii pe cele 2 porturi de iesire.
        this.pOutput1.write(objStudent.toString());
        this.pOutput1.newLine();
        this.pOutput1.flush();

        this.pOutput2.write(objStudent.toString());
        this.pOutput2.newLine();
        this.pOutput2.flush();
    }
}
