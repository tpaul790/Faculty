/*
 * Cerința 1: Comunicare Blocantă Punct-la-Punct (MPI_Send / MPI_Recv)
 * Problema: Distribuția și Agregarea Secvențială a SumeiVrem să calculăm suma elementelor dintr-un array mare,
 * folosind o metodă secvențială, unde datele circulă între procese strict unul câte unul.
 * Obiectiv: Procesul $0$ inițializează un array mare. Fiecare proces de la $1$ la $N-1$ primește o porțiune din array
 * de la predecesorul său, calculează suma locală și trimite suma locală plus restul array-ului
 * (pentru care nu este responsabil) către succesorul său.
 */


#include <iostream>
#include <vector>
#include <numeric>
#include <mpi.h>

const int CHUNK_SIZE = 100;
const int TAG_DATA = 0;
const int TAG_SUM = 1;

using namespace std;

void init(vector<int>& arr) {
    srand(0);
    for (int i = 0; i < arr.size(); i++) {
        arr[i] = rand() % 100 + 1;
    }
}

long long sequential_sum(const vector<int>& arr) {
    long long sum = 0;
    for (int x : arr) {
        sum += x;
    }
    return sum;
}

int main(int argc, char** argv) {

    MPI_Init(&argc, &argv);

    int size, rank;
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);

    if (size < 2) {
        if (rank == 0) {
            cerr << "Eroare: Sunt necesare minim 2 procese (mpirun -np 2...)." << endl;
        }
        MPI_Finalize();
        return 1;
    }

    int worker_count = size - 1;
    const int total_elements = CHUNK_SIZE * worker_count;

    if (rank == 0) {
        vector<int> global_arr(total_elements);
        init(global_arr);

        long long expected_sum = sequential_sum(global_arr);

        cout << "[Root] Trimite " << total_elements << " elemente catre Rank 1." << endl;
        MPI_Send(global_arr.data(), total_elements, MPI_INT, 1, TAG_DATA, MPI_COMM_WORLD);

        long long final_sum = 0;

        for (int i = 0; i < worker_count; i++) {
            long long partial_sum = 0;
            MPI_Recv(&partial_sum, 1, MPI_LONG_LONG, MPI_ANY_SOURCE, TAG_SUM, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            final_sum += partial_sum;
        }

        cout << "\n[Root] Suma paralela finala: " << final_sum << endl;
        cout << "[Root] Suma secventiala (ref): " << expected_sum << endl;

        if (final_sum == expected_sum) {
            cout << "Verificare: SUMA ESTE CORECTA." << endl;
        } else {
            cout << "Verificare: EROARE LA SUMA!" << endl;
        }
    }
    else {
        int elements_remaining = CHUNK_SIZE * (size - rank);

        vector<int> received_arr(elements_remaining);

        MPI_Recv(received_arr.data(), elements_remaining, MPI_INT, rank - 1, TAG_DATA, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        long long local_sum = 0;
        for (int i = 0; i < CHUNK_SIZE; i++) {
            local_sum += received_arr[i];
        }

        cout << "[Rank " << rank << "] S-a primit array de " << elements_remaining
             << " elemente. Suma locala calculata: " << local_sum << endl;

        MPI_Send(&local_sum, 1, MPI_LONG_LONG, 0, TAG_SUM, MPI_COMM_WORLD);

        if (rank < size - 1) {
            int elements_to_send = elements_remaining - CHUNK_SIZE;
            MPI_Send(received_arr.data() + CHUNK_SIZE, elements_to_send, MPI_INT, rank + 1, TAG_DATA, MPI_COMM_WORLD);
        }
    }

    MPI_Finalize();
    return 0;
}