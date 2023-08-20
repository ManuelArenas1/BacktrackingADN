import java.util.*;

class MutadorADN {
    private String secuencia;
    private int longitud;
    private String nucleotidos = "AGCT";
    private Map<Pair<String, Integer>, Set<String>> memoization = new HashMap<>();

    public MutadorADN(String sec, int len) {
        secuencia = sec;
        longitud = len;
    }

    public List<String> generarMutaciones(int cambiosPorMutacion) {
        Set<String> mutaciones = new HashSet<>();
        generarMutacionesRecursivas(secuencia, cambiosPorMutacion, mutaciones);

        List<String> mutacionesOrdenadas = new ArrayList<>(mutaciones);
        Collections.sort(mutacionesOrdenadas);
        return mutacionesOrdenadas;
    }

    private void generarMutacionesRecursivas(String secuenciaActual, int cambiosRestantes, Set<String> resultado) {
        if (cambiosRestantes == 0) {
            resultado.add(secuenciaActual);
            return;
        }

        Pair<String, Integer> memoKey = new Pair<>(secuenciaActual, cambiosRestantes);
        if (memoization.containsKey(memoKey)) {
            resultado.addAll(memoization.get(memoKey));
            return;
        }

        Set<String> nuevasMutaciones = new HashSet<>();

        for (int i = 0; i < secuenciaActual.length(); ++i) {
            for (char nucleotido : nucleotidos.toCharArray()) {
                if (secuenciaActual.charAt(i) != nucleotido) {
                    StringBuilder sb = new StringBuilder(secuenciaActual);
                    sb.setCharAt(i, nucleotido);
                    String secuenciaMutada = sb.toString();
                    generarMutacionesRecursivas(secuenciaMutada, cambiosRestantes - 1, resultado);
                    nuevasMutaciones.add(secuenciaMutada);
                }
            }
        }

        memoization.put(memoKey, nuevasMutaciones);
        resultado.addAll(nuevasMutaciones);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numSecuencias = scanner.nextInt();

        if (numSecuencias > 50) {
            System.out.println();
        } else {
            List<String> secuencias = new ArrayList<>();
            List<Integer> cambiosPorSecuencia = new ArrayList<>();

            for (int i = 0; i < numSecuencias; ++i) {
                int longitudSecuencia = scanner.nextInt();
                int cambiosPorMutacion = scanner.nextInt();
                String secuencia = scanner.next();

                if (longitudSecuencia > 10 || cambiosPorMutacion > 5) {
                    System.out.println();
                    break;
                }

                secuencias.add(secuencia);
                cambiosPorSecuencia.add(cambiosPorMutacion);
            }

            for (int i = 0; i < secuencias.size(); ++i) {
                MutadorADN mutador = new MutadorADN(secuencias.get(i), secuencias.get(i).length());
                List<String> secuenciasMutadas = mutador.generarMutaciones(cambiosPorSecuencia.get(i));

                System.out.println(secuenciasMutadas.size());

                for (String secuenciaMutada : secuenciasMutadas) {
                    System.out.println(secuenciaMutada);
                }
            }
        }
    }
}

class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.key, key) && Objects.equals(p.value, value);
    }
}
