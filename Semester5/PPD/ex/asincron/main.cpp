/*
Problema: Suprapunerea Comunicării cu un Calcul Ierarhic
Vrem să calculăm suma elementelor dintr-un array folosind o structură de tip arbore (adică, procesele se adună în perechi),
dar dorim să ascundem latența comunicării.
Obiectiv: Fiecare proces primește o bucată din array, calculează suma locală,
iar apoi, în loc să aștepte, inițiază imediat trimiterea sumei sale către părinte (conform logicii arborelui) și,
în timp ce transferul are loc, execută o sarcină intensivă de calcul care nu depinde de rezultatul trimis.
*/

#include <iostream>
#include <vector>
#include <numeric>
#include <cmath>
#include <chrono>
#include <mpi.h>

using namespace std;

const int GLOBAL_BUFF_SIZE = 1000000;
const long long WORK_CYCLES = 5000000;

void init(vector<int>& v) {
    srand(0);
    v.resize(GLOBAL_BUFF_SIZE);
    for (int i = 0; i < GLOBAL_BUFF_SIZE; i++) {
        v[i] = rand() % 10 + 1;
    }
}

long long sequential_sum(const vector<int>& v) {
    long long s = 0;
    for (int x : v) {
        s += x;
    }
    return s;
}

void simulate_work() {
    volatile double result = 1.0;
    for (long long i = 0; i < WORK_CYCLES; ++i) {
        result = sin(result) * cos(result);
    }
}

int main(int argc, char** argv) {

    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (GLOBAL_BUFF_SIZE % size != 0) {
        if (rank == 0) {
            cerr << "Eroare: GLOBAL_BUFF_SIZE (" << GLOBAL_BUFF_SIZE << ") trebuie sa fie divizibil cu numarul de procese (" << size << ")." << endl;
        }
        MPI_Finalize();
        return 1;
    }

    const int LOCAL_BUFF_SIZE = GLOBAL_BUFF_SIZE / size;
    vector<int> global_buff(GLOBAL_BUFF_SIZE);
    vector<int> local_buff(LOCAL_BUFF_SIZE);
    long long global_expected_sum = 0;

    if (rank == 0) {
        init(global_buff);
        global_expected_sum = sequential_sum(global_buff);
    }

    MPI_Scatter(global_buff.data(), LOCAL_BUFF_SIZE, MPI_INT,
                local_buff.data(), LOCAL_BUFF_SIZE, MPI_INT,
                0, MPI_COMM_WORLD);

    long long local_sum = 0;
    for (int x : local_buff) {
        local_sum += x;
    }


    const int child1 = rank * 2 + 1;
    const int child2 = rank * 2 + 2;

    vector<MPI_Request> recv_reqs(2);
    vector<long long> child_sums(2);
    int children_count = 0;

    if (child1 < size) {
        MPI_Irecv(&child_sums[children_count], 1, MPI_LONG_LONG, child1, 0, MPI_COMM_WORLD, &recv_reqs[children_count]);
        children_count++;
    }

    if (child2 < size) {
        MPI_Irecv(&child_sums[children_count], 1, MPI_LONG_LONG, child2, 0, MPI_COMM_WORLD, &recv_reqs[children_count]);
        children_count++;
    }

    auto start_work = chrono::high_resolution_clock::now();
    simulate_work();
    auto end_work = chrono::high_resolution_clock::now();
    auto work_duration = chrono::duration_cast<chrono::milliseconds>(end_work - start_work).count();

    cout << "[Rank " << rank << "] Calcul intensiv finalizat (" << work_duration << "ms). Astept finalizarea comunicarii." << endl;

    if (children_count > 0) {
        cout << "[Rank " << rank << "] Astept finalizarea primirii de la copii... (Waitall)" << endl;
        MPI_Waitall(children_count, recv_reqs.data(), MPI_STATUSES_IGNORE);

        for (int i = 0; i < children_count; ++i) {
            local_sum += child_sums[i];
        }
        cout << "[Rank " << rank << "] Sumele copiilor acumulate. Suma agregata: " << local_sum << endl;
    }

    MPI_Request send_req;
    if (rank != 0) {
        const int parent_rank = (rank - 1) / 2;

        MPI_Isend(&local_sum, 1, MPI_LONG_LONG, parent_rank, 0, MPI_COMM_WORLD, &send_req);

        cout << "[Rank " << rank << "] Isend initiat catre Rank " << parent_rank << " cu suma agregata. Astept finalizare trimitere." << endl;

        MPI_Wait(&send_req, MPI_STATUS_IGNORE);
    }

    if (rank == 0) {
        cout << "\n=================================================" << endl;
        cout << "Suma paralela finala (Arbore): " << local_sum << endl;
        cout << "Suma secventiala (Referinta): " << global_expected_sum << endl;

        if (local_sum == global_expected_sum) {
            cout << "REZULTAT VALID: Suma este CORECTA." << endl;
        } else {
            cout << "EROARE: Suma nu este corecta. Diferenta: " << global_expected_sum - local_sum << endl;
        }
        cout << "=================================================" << endl;
    }

    MPI_Finalize();
    return 0;
}