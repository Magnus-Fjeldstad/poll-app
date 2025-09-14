<script setup>
import {ref} from 'vue';
import {createPoll} from '../api';

const props = defineProps({
  creatorId: {type: String, required: true},
});

const question = ref('');
const validUntilLocal = ref('');
const options = ref([
  {caption: 'Yes please', presentationOrder: 1},
  {caption: 'Absolutely not', presentationOrder: 2},
]);
const loading = ref(false);
const error = ref('');
const createdPollId = ref('');

function addOption() {
  options.value.push({caption: '', presentationOrder: options.value.length + 1});
}

function removeOption(idx) {
  options.value.splice(idx, 1);
  options.value.forEach((o, i) => (o.presentationOrder = i + 1));
}

function toIsoUtc(datetimeLocal) {
  if (!datetimeLocal) return null;
  const d = new Date(datetimeLocal);
  if (isNaN(+d)) return null;
  return d.toISOString();
}

async function submit() {
  error.value = '';
  createdPollId.value = '';
  if (!question.value.trim() || options.value.length < 2) {
    error.value = 'Need a question and at least two options';
    return;
  }
  loading.value = true;
  try {
    const body = {
      creatorId: props.creatorId,
      question: question.value.trim(),
      validUntil: toIsoUtc(validUntilLocal.value),
      options: options.value.map((o, i) => ({
        caption: o.caption,
        presentationOrder: i + 1,
      })),
    };
    const created = await createPoll(body);
    createdPollId.value = created.id;
  } catch (e) {
    error.value = e.message ?? String(e);
  } finally {
    loading.value = false;
  }
}

function clearValidUntil() {
  validUntilLocal.value = '';
}
</script>

<template>
  <div class="card">
    <h2>Create Poll</h2>

    <label class="lbl">Question</label>
    <input class="txt" v-model="question" placeholder="Your poll question"/>

    <label class="lbl">Valid until</label>
    <div class="row">
      <input class="txt" type="datetime-local" v-model="validUntilLocal"/>
      <button class="btn ghost" @click="clearValidUntil">Clear</button>
    </div>

    <div class="opts">
      <div class="opt-row" v-for="(opt, idx) in options" :key="idx">
        <input class="txt" v-model="opt.caption" :placeholder="`Option #${idx+1}`"/>
        <button class="btn danger" @click="removeOption(idx)" :disabled="options.length<=2">
          Remove
        </button>
      </div>
      <button class="btn add" @click="addOption">+ Add option</button>
    </div>

    <div class="actions">
      <button class="btn primary" @click="submit" :disabled="loading">
        {{ loading ? 'Creating…' : 'Create poll' }}
      </button>
      <span v-if="error" class="error">{{ error }}</span>
      <span v-if="createdPollId" class="ok">Created: {{ createdPollId }}</span>
    </div>
  </div>
</template>

<style scoped>
.card {
  padding: 28px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(14px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.35);
}

h2 {
  margin-bottom: 16px;
  font-size: 20px;
  font-weight: 700;
}

.lbl {
  display: block;
  margin: 16px 0 6px;
  font-size: 14px;
  color: #cbd5e1;
}

.txt {
  width: 100%;
  padding: 12px 14px;
  border-radius: 12px;
  border: none;
  background: rgba(255, 255, 255, 0.12);
  color: #f8fafc;
  outline: none;
}

.txt:focus {
  background: rgba(255, 255, 255, 0.18);
  box-shadow: 0 0 0 2px #6366f1;
}

.row {
  display: flex;
  gap: 10px;
  align-items: center;
}

.opts {
  margin-top: 18px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.opt-row {
  display: flex;
  gap: 10px;
}

.btn {
  border: none;
  padding: 10px 16px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

.btn.primary {
  background: linear-gradient(90deg, #6366f1, #22d3ee);
  color: white;
}

.btn.primary:hover {
  opacity: 0.9;
}

.btn.danger {
  background: #ef4444;
  color: white;
}

.btn.add {
  background: #0ea5e9;
  color: white;
}

.btn.ghost {
  background: rgba(255, 255, 255, 0.12);
  color: #f8fafc;
}

.actions {
  margin-top: 20px;
  display: flex;
  gap: 14px;
  align-items: center;
}

.error {
  color: #fca5a5;
}

.ok {
  color: #86efac;
}
</style>
