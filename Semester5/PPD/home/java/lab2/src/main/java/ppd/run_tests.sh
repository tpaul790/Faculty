#!/bin/bash

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <num_runs> <method> <type (if applicable)>"
    exit 1
fi

RUNS=$1
METHOD=$2
TYPE=${3:-""}
RESULTS_CSV="data/results/execution_results.csv"
JAVA_CP="../../../../build/classes/java/main"

if [ ! -f "$RESULTS_CSV" ]; then
      echo "N, M, K, Method, Type, Thread Count, Output File, Average Execution Time (ms)" > "$RESULTS_CSV"
fi

declare -a TEST_CASES=("10 10 3" "1000 1000 3" "10000 10000 3")
declare -a THREAD_COUNT=( "2" "2 4 8 16" "2 4 8 16")
declare -a COMPUTATION_METHODS=("INLINE_SEQUENTIAL" "INLINE_PARALLEL")
declare -a SYNC_TYPES=("BARRIER" "FLAG")

if [[ "$METHOD" != "SEQUENTIAL" && ! ${COMPUTATION_METHODS[*]} =~ ${METHOD} ]]; then
  echo "Invalid method. Valid methods are: SEQUENTIAL, ${COMPUTATION_METHODS[*]}"
  exit 1
fi

if [[ "$METHOD" == "INLINE_PARALLEL" && ! ${SYNC_TYPES[*]} =~ ${TYPE} ]]; then
  echo "For INLINE_PARALLEL method, valid types are: ${SYNC_TYPES[*]}"
  exit 1
elif [[ "$METHOD" == "INLINE_SEQUENTIAL" && "$TYPE" != "" ]]; then
  echo "INLINE_SEQUENTIAL method does not require a type. Do not pass a type argument."
  exit 1
fi

for i in "${!TEST_CASES[@]}"; do
  test_case=${TEST_CASES[$i]}
  threads=${THREAD_COUNT[$i]}

  read -r N M K <<< "$test_case"
  input_file="input_${N}_${M}_${K}.txt"

  if [ "$METHOD" == "SEQUENTIAL" ] || [ "$METHOD" == "INLINE_SEQUENTIAL" ]; then
    exec_time=$(java -cp "$JAVA_CP" ppd.Main "$input_file" "1" "$METHOD")
    if [ $? -ne 0 ]; then
      echo "Execution failed for $input_file using $METHOD method. Output is invalid. Skipping further tests."
      continue
    fi

    output_file="output_${METHOD}_${N}_${M}_${K}.txt"
    echo "$N, $M, $K, $METHOD, None, 1, $output_file, $exec_time" >> "$RESULTS_CSV"
    echo "Executed $METHOD for test case $test_case - $thread_count threads: Time = $exec_time ms"
  else
    for thread_count in $threads; do
      total_time=0
      for (( run=1; run<=RUNS; run++ )); do
        exec_time=$(java -cp "$JAVA_CP" ppd.Main "$input_file" "$thread_count" "$METHOD" "$TYPE")
        if [ $? -ne 0 ]; then
          echo "Execution failed for $input_file using $METHOD method. Output is invalid. Skipping further tests."
          continue
        fi
        total_time=$(echo "$total_time + $exec_time" | bc)
      done

      avg_time=$(echo "scale=2; $total_time / $RUNS" | bc)
      output_file="output_${METHOD}_${N}_${M}_${K}_${thread_count}_${TYPE}.txt"
      echo "$N, $M, $K, $METHOD, $TYPE, $thread_count, $output_file, $avg_time" >> "$RESULTS_CSV"
      echo "Executed $METHOD $TYPE for test case $test_case - $thread_count threads: Time = $exec_time ms"
    done
  fi
done