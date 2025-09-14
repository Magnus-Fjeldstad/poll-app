<script setup>
import {onMounted, ref} from 'vue';
import CreatePoll from './components/CreatePoll.vue';
import PollVote from './components/PollVote.vue';
import {createUser, listPolls} from './api';

const currentUserId = ref('');
const polls = ref([]);

async function bootstrap() {
  polls.value = await listPolls();
  try {
    const u = await createUser('ola', 'ola@example.com');
    currentUserId.value = u.id;
  } catch {
  }
}

onMounted(bootstrap);
</script>

<template>
  <main class="app">
    <header class="app-header">
      <h1>PollApp</h1>
    </header>

    <section class="content">
      <div class="left">
        <CreatePoll v-if="currentUserId" :creator-id="currentUserId"/>
      </div>

      <div class="right">
        <h2>All Polls</h2>
        <div v-if="polls.length === 0" class="empty">No polls yet</div>
        <div v-else class="poll-list">
          <PollVote
            v-for="p in polls"
            :key="p.id"
            :poll-id="p.id"
            :current-user-id="currentUserId"
          />
        </div>
      </div>
    </section>
  </main>
</template>

<style scoped>
.app {
  min-height: 100vh;
  background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
  color: #f8fafc;
  font-family: 'Inter', system-ui, sans-serif;
  padding: 40px;
}

.app-header {
  text-align: center;
  margin-bottom: 40px;
}

.app-header h1 {
  font-size: 36px;
  font-weight: 800;
  background: linear-gradient(90deg, #6366f1, #22d3ee);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
}

.empty {
  padding: 20px;
  text-align: center;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.05);
  font-style: italic;
}

.poll-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

@media (max-width: 900px) {
  .content {
    grid-template-columns: 1fr;
  }
}
</style>
