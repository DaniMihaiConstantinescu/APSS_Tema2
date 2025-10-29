import java.io.*;
import java.util.Arrays;
import java.util.List;

public class SystemMain {

    /**
     * Creaza componentele si porneste sistemul.
     * Sunt asteptati doi parametrii de intrare:
     * primul: numele fisierului de intrare ce contine inregistrarile corespunzatoare studentilor candidati,
     * al doilea: numele fisierului de iesire ca va contine inregistrarile studentilor acceptati.
     *
     * @param args array cu parametrii de intrare
     */
    public static void main(String[] args) {
        // Verificarea numarului parametrilor de intrare.
        if (args.length != 2) {
            System.out.println("Numar incorect de parametri");
            System.out.println("Utilizare corecta: java SystemMain <fisier_de_intrare> <fisier_de_iesire>");
            System.exit(1);
        }

        // Verificarea existentei fisierului de intrare.
        if (!new File(args[0]).exists()) {
            System.out.println("Could not find " + args[0]);
            System.exit(1);
        }

        // Vertificarea existentei directorului parinte al fisierului de iesire.
        // Crearea acestuia daca e necesar.
        File parentFile = new File(args[1]).getAbsoluteFile().getParentFile();
        if (!parentFile.exists() && !parentFile.mkdir()) {
            System.out.println("Nu s-a putut crea directorul parinte " + args[1]);
            System.exit(1);
        }

        try {
            // _____________________________________________________________________
            // Crearea si legarea componentelor si conectorilor
            // _____________________________________________________________________

            // Crearea conductelor (de fapt, a rolurilor).
            System.out.println("Controller: Creare conectori (roluri)...");
            PipedWriter objTemp;

            BufferedWriter roleInputToMultiCourseSource = new BufferedWriter(objTemp = new PipedWriter());
            BufferedReader roleInputToMultiCourseSync   = new BufferedReader(new PipedReader(objTemp));

            BufferedWriter roleNonIADCSource = new BufferedWriter(objTemp = new PipedWriter());
            BufferedReader roleNonIADCSync   = new BufferedReader(new PipedReader(objTemp));

            BufferedWriter roleIADCAcceptedSource = new BufferedWriter(objTemp = new PipedWriter());
            BufferedReader roleIADCAcceptedSync   = new BufferedReader(new PipedReader(objTemp));

            BufferedWriter roleCourse1322AcceptedSource = new BufferedWriter(objTemp = new PipedWriter());
            BufferedReader roleCourse1322AcceptedSync   = new BufferedReader(new PipedReader(objTemp));

            BufferedReader roleInputFileSync    = new BufferedReader(new FileReader(args[0]));
            BufferedWriter roleOutputFileSource = new BufferedWriter(new FileWriter(args[1]));


            // Crearea filtrelor (transferul rolurilor ca parametrii, pentru a fi legati
            // la porturile fiecarui filtru).
            System.out.println("Controller: Creare componente ...");
            List<Integer> coursesToCheck = Arrays.asList(17651, 21701);
            MultiCourseFilter filterMultiCourse =
                    new MultiCourseFilter("MultiCourse", roleInputFileSync, roleInputToMultiCourseSource, coursesToCheck);
            SplitFilter filterSplitIADC =
                    new SplitFilter("SplitIADC", roleInputToMultiCourseSync,
                            roleIADCAcceptedSource, roleNonIADCSource, "IADC");
            CourseFilter filterCourse1322 =
                    new CourseFilter("Course1322", roleNonIADCSync, roleCourse1322AcceptedSource, 1322);
            MergeFilter filterMergeAccepted =
                    new MergeFilter("MergeAccepted", roleIADCAcceptedSync, roleCourse1322AcceptedSync, roleOutputFileSource);


            // _____________________________________________________________________
            // Executarea sistemului
            // _____________________________________________________________________

            // Start all filters.
            System.out.println("Controller: Pornire filtre ...");
            filterMultiCourse.start();
            filterSplitIADC.start();
            filterCourse1322.start();
            filterMergeAccepted.start();

            // Asteapta pana la terminarea datelor de pe lanturile conductelor si filtrelor.
            // Ordinea de verificare, de la intrare la iesire, este importanta pentru a evita problemele de concurenta.
            // Analizati ce s-ar intampla daca lantul pipe-and-filter ar fi circular.
            while (roleInputFileSync.ready() || filterMultiCourse.busy()
                    || roleInputToMultiCourseSync.ready() || filterSplitIADC.busy()
                    || roleNonIADCSync.ready() || filterCourse1322.busy()
                    || roleIADCAcceptedSync.ready() || roleCourse1322AcceptedSync.ready()
                    || filterMergeAccepted.busy()
            ) {
                // Afiseaza un semnal de feedback signal si transfera controlul pentru planifcarea altui fir de executie.
                System.out.print('.');
                Thread.yield();
            }

            // _____________________________________________________________________
            // Curatarea sistemului
            // _____________________________________________________________________

            // Distrugerea tuturor filtrelor.
            System.out.println("Controller: Distrugerea tuturor componentelor ...");
            filterMultiCourse.interrupt();
            filterSplitIADC.interrupt();
            filterCourse1322.interrupt();
            filterMergeAccepted.interrupt();

            while (filterMultiCourse.isAlive() || filterSplitIADC.isAlive()
                    || filterCourse1322.isAlive() || filterMergeAccepted.isAlive()
            ) {
                // Afiseaza un semnal de feedback si transfera controlul planificatorului de fire de execuitie.
                System.out.print('.');
                Thread.yield();
            }

            // Distrugerea tuturor conductelor.
            System.out.println("Controller: Distrugerea tuturor conectorilor ...");
            roleInputFileSync.close();
            roleOutputFileSource.close();
            roleInputToMultiCourseSource.close();
            roleInputToMultiCourseSync.close();
            roleNonIADCSource.close();
            roleNonIADCSync.close();
            roleIADCAcceptedSource.close();
            roleIADCAcceptedSync.close();
            roleCourse1322AcceptedSource.close();
            roleCourse1322AcceptedSync.close();
        }
        catch (Exception e) {
            // Afisarea de informatii pentru debugging.
            System.out.println("Exceptie aparuta in SystemMain.");
            e.printStackTrace();
            System.exit(1);
        }

        // Final!
        System.out.println("Controller: Final!");
    }
}
