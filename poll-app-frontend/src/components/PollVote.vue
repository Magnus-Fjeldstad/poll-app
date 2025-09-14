<script setup>
import {computed, onMounted, ref, watch} from 'vue';
import {getPoll, optionStats, voteOption} from '../api';

const props = defineProps({
  pollId: {type: String, required: true},
  currentUserId: {type: String, required: true},
});

const poll = ref(null);
const loading = ref(false);
const error = ref('');
const stats = ref({});

async function loadPoll() {
  loading.value = true;
  error.value = '';
  try {
    poll.value = await getPoll(props.pollId);
    await refreshAllStats();
  } catch (e) {
    error.value = e.message ?? String(e);
  } finally {
    loading.value = false;
  }
}

async function refreshAllStats() {
  if (!poll.value) return;
  const s = {};
  await Promise.all(
    (poll.value.options ?? []).map(async (opt) => {
      const st = await optionStats(opt.id);
      s[opt.id] = st;
    }),
  );
  stats.value = s;
}

async function onVote(optionId, value) {
  try {
    await voteOption(optionId, props.currentUserId, value);
    const st = await optionStats(optionId);
    stats.value = {...stats.value, [optionId]: st};
  } catch (e) {
    alert(e.message ?? String(e));
  }
}

const sortedOptions = computed(() =>
  (poll.value?.options ?? []).slice().sort((a, b) => a.presentationOrder - b.presentationOrder),
);
onMounted(loadPoll);
watch(() => props.pollId, loadPoll);
</script>

<template>
  <div class="poll" v-if="poll">
    <div class="poll-header">
      <span class="badge">#{{ poll.id.slice(0, 6) }}</span>
      <h3>{{ poll.question }}</h3>
    </div>

    <div class="options">
      <div class="option" v-for="opt in sortedOptions" :key="opt.id">
        <span>{{ opt.caption }}</span>
        <div class="actions">
          <button class="vote up" @click="onVote(opt.id, +1)">▲</button>
          <button class="vote down" @click="onVote(opt.id, -1)">▼</button>
          <span class="votes">{{ stats[opt.id]?.votes ?? 0 }} votes</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.poll {
  padding: 24px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(14px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.35);
}

.poll-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.poll-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.badge {
  padding: 4px 8px;
  border-radius: 999px;
  font-size: 12px;
  background: linear-gradient(90deg, #6366f1, #22d3ee);
  color: white;
}

.options {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.05);
  transition: background 0.2s;
}

.option:hover {
  background: rgba(255, 255, 255, 0.1);
}

.actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.vote {
  border: none;
  padding: 6px 10px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  color: white;
  transition: transform 0.1s, background 0.2s;
}

.vote.up {
  background: #22c55e;
}

.vote.down {
  background: #ef4444;
}

.vote:hover {
  transform: translateY(-1px);
}

.votes {
  font-size: 13px;
  color: #cbd5e1;
}
</style>
