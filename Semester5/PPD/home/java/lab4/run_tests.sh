#!/bin/bash

if [ "$#" -lt 1 ]; then
    echo "Usage: $0 <method> <num_threads> <num_readers>"
    exit 1
fi

METHOD=$1
JAVA_CP="target/classes"

RUNS=10
RESULTS_CSV="src/main/java/ppd/data/results/execution_results.csv"
if [ ! -f "$RESULTS_CSV" ]; then
      echo "Method, Thread Count, Reader Count, Average Execution Time (ms)" > "$RESULTS_CSV"
fi

total_time=0

if [ "$METHOD" == "SEQUENTIAL" ]; then
    RUN_EXEC="java -cp $JAVA_CP ppd.Main SEQUENTIAL"
elif [ "$METHOD" == "PARALLEL" ]; then
    NUM_PROCESSES=$2
    NUM_READERS=$3
    RUN_EXEC="java -cp $JAVA_CP ppd.Main PARALLEL $NUM_PROCESSES $NUM_READERS"
else
    echo "Invalid method: $METHOD"
    exit 1
fi

for (( run=1; run<=RUNS; run++ )); do
    exec_time=$($RUN_EXEC)
    if [ $? -ne 0 ]; then
        echo "Execution failed for $METHOD method. Output is invalid. Skipping further tests."
        continue
    fi
    total_time=$(echo "$total_time + $exec_time" | bc)
#    echo "Execution $run: Time = $exec_time ms"
done

average_time=$(echo "scale=2; $total_time / $RUNS" | bc)

echo "$METHOD, $NUM_PROCESSES, $NUM_READERS, $average_time" >> "$RESULTS_CSV"
echo "Executed $METHOD with $NUM_PROCESSES processes and $NUM_READERS readers: Average Time = $average_time ms"

