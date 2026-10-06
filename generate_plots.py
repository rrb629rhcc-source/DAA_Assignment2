import pandas as pd
import matplotlib.pyplot as plt
import os

if os.path.isfile('results/plots'):
    os.remove('results/plots')
os.makedirs('results/plots', exist_ok=True)

# Читаем CSV без автоматического разбиения по именам колонок
with open('results/results.csv', 'r', encoding='utf-8') as f:
    lines = [line.strip().split(',') for line in f if line.strip()]

data = []
for row in lines[1:]: # Пропускаем заголовок
    if len(row) < 8:
        continue

    workload = row[0]
    variant = row[1]
    structure = row[2]
    n = int(row[3])

    # Если время разделилось на две колонки (0 и 6934 -> 0.6934)
    if len(row) >= 9 and row[4] == '0' and row[5].isdigit():
        time_ms = float(f"0.{row[5]}")
        steps = int(row[6])
        moves = int(row[7])
        comparisons = int(row[8])
    else:
        time_ms = float(f"{row[4]}.{row[5]}") if len(row) > 8 else float(row[4].replace(',', '.'))
        steps = int(row[5]) if len(row) <= 8 else int(row[6])
        moves = int(row[6]) if len(row) <= 8 else int(row[7])
        comparisons = int(row[7]) if len(row) <= 8 else int(row[8])

    data.append({
        'workload': workload,
        'variant': variant,
        'structure': structure,
        'n': n,
        'time_ms': time_ms,
        'steps': steps,
        'moves': moves,
        'comparisons': comparisons
    })

df = pd.DataFrame(data)

# 1. W1 - Random Access
w1 = df[df['workload'] == 'W1']
if not w1.empty:
    plt.figure(figsize=(8, 5))
    for struct in w1['structure'].unique():
        sub = w1[w1['structure'] == struct].sort_values('n')
        plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=struct)
    plt.title('W1: Random Access (Time vs n)')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.xscale('log')
    plt.grid(True)
    plt.legend()
    plt.savefig('results/plots/w1_time.png')
    plt.close()

# 2. W2 - Search
w2 = df[df['workload'] == 'W2']
if not w2.empty:
    plt.figure(figsize=(8, 5))
    for struct in w2['structure'].unique():
        sub = w2[w2['structure'] == struct].sort_values('n')
        plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=struct)
    plt.title('W2: Search contains(x) (Time vs n)')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.xscale('log')
    plt.grid(True)
    plt.legend()
    plt.savefig('results/plots/w2_time.png')
    plt.close()

# 3. W3 - Insert & Remove
w3 = df[df['workload'] == 'W3']
if not w3.empty:
    plt.figure(figsize=(8, 5))
    for (struct, var), sub in w3.groupby(['structure', 'variant']):
        sub = sub.sort_values('n')
        plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=f'{struct} ({var})')
    plt.title('W3: Insert & Remove (Time vs n)')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.xscale('log')
    plt.grid(True)
    plt.legend()
    plt.savefig('results/plots/w3_time.png')
    plt.close()

# 4. W4 - MinHeap
w4 = df[df['workload'] == 'W4'].sort_values('n')
if not w4.empty:
    plt.figure(figsize=(8, 5))
    plt.plot(w4['n'], w4['time_ms'], marker='o', linewidth=2, color='green', label='MinHeap')
    plt.title('W4: MinHeap Operations (Time vs n)')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.xscale('log')
    plt.grid(True)
    plt.legend()
    plt.savefig('results/plots/w4_time.png')
    plt.close()

print("Графики успешно сгенерированы!")