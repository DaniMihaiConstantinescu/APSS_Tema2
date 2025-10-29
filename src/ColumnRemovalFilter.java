import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Un filtru de eliminare a coloanelor inutile din raspuns cu un port de intrare si un port de iesire.
 * El transfera la portul de iesire datele disponibile pe portul de intrare
 * eliminand toate coloanele mai putin numele si programul.
 */
public class ColumnRemovalFilter extends Filter {

    /**
     * Portul de intrare.
     **/
    protected BufferedReader pInput;

    /**
     * Portul de iesire.
     **/
    protected BufferedWriter pOutput;

    /**
     * Construieste un filtru pentru eliminarea unor coloane cu numele dat.
     * Porturile sunt impachetate intr-un flux de caratere buffer-at.
     *
     * @param sName   sirul ce reprezinta numele filtrului
     * @param pInput1 portul de intrare al acestui filtru
     * @param pOutput portul de iesire al acestui filtru
     */
    public ColumnRemovalFilter(String sName,
                       BufferedReader pInput1, BufferedWriter pOutput) {
        // Executarea constructorului clasei parinte.
        super(sName);

        // Initializarea porturilor de intrare si de iesire.
        this.pInput = pInput1;
        this.pOutput = pOutput;
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

        // Scrierea inregistrarii la portul de iesire.
        this.pOutput.write(String.format("%s %s", objStudent.sName, objStudent.sProgram));
        this.pOutput.newLine();
        this.pOutput.flush();
    }
}
