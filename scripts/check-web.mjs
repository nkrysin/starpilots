import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';

const assets = fs.readdirSync('assets', { recursive: true })
  .filter((name) => name !== 'assets.txt' && fs.statSync(path.join('assets', name)).isFile())
  .map((name) => name.split(path.sep).join('/')).sort();
assert.deepEqual(fs.readFileSync('assets/assets.txt', 'utf8').trim().split('\n'), assets);
const output = 'teavm/build/dist/webapp';
assert(fs.statSync(path.join(output, 'app.js')).size > 0, 'Missing compiled game');
for (const asset of assets) {
  assert(fs.statSync(path.join(output, 'assets', asset)).isFile(), `Missing web asset: ${asset}`);
}
const campaigns = JSON.parse(fs.readFileSync('assets/campaigns/campaigns.json', 'utf8'));
for (const campaign of campaigns) {
  JSON.parse(fs.readFileSync(path.join(output, 'assets', 'campaigns', `${campaign}.json`), 'utf8'));
}
console.log(`Verified compiled game, ${assets.length} assets and ${campaigns.length} campaigns.`);
