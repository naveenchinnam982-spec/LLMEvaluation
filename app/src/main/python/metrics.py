import math
import re
import collections

def tokenize(text):
    return re.findall(r'\w+', text.lower())

def calculate_bleu(reference, candidate):
    ref_tokens = tokenize(reference)
    can_tokens = tokenize(candidate)

    if not ref_tokens or not can_tokens:
        return 0

    # Simple 1-gram precision (Simplified BLEU)
    matches = collections.Counter(ref_tokens) & collections.Counter(can_tokens)
    score = sum(matches.values()) / len(can_tokens)

    # Apply brevity penalty
    bp = math.exp(min(0, 1 - len(ref_tokens) / len(can_tokens)))

    return score * bp * 100

def calculate_rouge(reference, candidate):
    ref_tokens = tokenize(reference)
    can_tokens = tokenize(candidate)

    if not ref_tokens or not can_tokens:
        return 0

    # Simplified ROUGE-1 Recall
    matches = collections.Counter(ref_tokens) & collections.Counter(can_tokens)
    recall = sum(matches.values()) / len(ref_tokens)

    return recall * 100

def calculate_perplexity(text):
    tokens = tokenize(text)
    if not tokens: return 0

    # Simulating perplexity using entropy of token distribution
    counts = collections.Counter(tokens)
    probs = [count / len(tokens) for count in counts.values()]
    entropy = -sum(p * math.log2(p) for p in probs)

    # Perplexity = 2^entropy
    ppl = math.pow(2, entropy)
    return min(100, ppl * 5) # Scale to 0-100 for display

def calculate_semantic_similarity(text1, text2):
    tokens1 = set(tokenize(text1))
    tokens2 = set(tokenize(text2))

    if not tokens1 or not tokens2: return 0

    intersection = tokens1.intersection(tokens2)
    union = tokens1.union(tokens2)

    return (len(intersection) / len(union)) * 100

def calculate_fluency(text):
    # Simulated grammar score based on basic checks
    sentences = re.split(r'[.!?]+', text)
    sentences = [s.strip() for s in sentences if s.strip()]

    if not sentences: return 0

    valid_sentences = 0
    for sent in sentences:
        # Check if starts with capital and ends with proper word
        if sent[0].isupper() and len(sent.split()) > 2:
            valid_sentences += 1

    return (valid_sentences / len(sentences)) * 100

def calculate_coherence(text):
    sentences = re.split(r'[.!?]+', text)
    sentences = [s.strip() for s in sentences if s.strip()]

    if len(sentences) < 2: return 100

    similarities = []
    for i in range(len(sentences) - 1):
        similarities.append(calculate_semantic_similarity(sentences[i], sentences[i+1]))

    return sum(similarities) / len(similarities)

def calculate_accuracy(reference, candidate):
    return calculate_semantic_similarity(reference, candidate)
