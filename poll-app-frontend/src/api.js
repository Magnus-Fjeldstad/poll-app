const BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080';

async function jfetch(path, opts = {}) {
  const res = await fetch(`${BASE}${path}`, {
    headers: {'Content-Type': 'application/json', ...(opts.headers ?? {})},
    ...opts,
  });
  if (!res.ok) {
    const txt = await res.text().catch(() => '');
    throw new Error(`HTTP ${res.status} ${res.statusText}: ${txt}`);
  }
  const ct = res.headers.get('content-type') || '';
  return ct.includes('application/json') ? res.json() : null;
}

/* Users */
export const createUser = (username, email) =>
  jfetch('/api/users', {method: 'POST', body: JSON.stringify({username, email})});

/* Polls */
export const listPolls = () => jfetch('/api/polls');
export const getPoll = (id) => jfetch(`/api/polls/${id}`);
export const createPoll = ({creatorId, question, validUntil, options}) =>
  jfetch('/api/polls', {
    method: 'POST',
    body: JSON.stringify({creatorId, question, validUntil, options}),
  });

/* Option votes (up/down) */
export const voteOption = (optionId, userId, value) =>
  jfetch(`/api/options/${optionId}/vote`, {
    method: 'PUT',
    body: JSON.stringify({userId, value}),
  });

export const optionStats = (optionId) => jfetch(`/api/options/${optionId}/stats`);
