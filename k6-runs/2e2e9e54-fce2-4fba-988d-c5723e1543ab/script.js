import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1,
  duration: '3s',
};

export default function () {
  const url = '/api/test';
  const method = 'GET';
  const payload = null;
  const params = {
    headers: { 'Content-Type': 'application/json' },
  };
  const bodyToSend = (method === 'POST' || method === 'PUT') ? JSON.stringify(payload) : null;
  const res = http.request(method, url, bodyToSend, params);
  check(res, { 'status is 200': (r) => r.status == 200 });
  sleep(1);
}
