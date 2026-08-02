import json
import metrics

def evaluate_responses(prompt, response_data_json):
    """
    response_data_json: JSON string of list of dicts: [{"model": "GPT-4", "text": "...", "reference": "..."}]
    """
    try:
        response_list = json.loads(response_data_json)
        results = []

        for item in response_list:
            model_name = item.get("model", "Unknown")
            candidate = item.get("text", "")
            reference = item.get("reference", prompt) # Use prompt as fallback reference

            bleu = metrics.calculate_bleu(reference, candidate)
            rouge = metrics.calculate_rouge(reference, candidate)
            perplexity = metrics.calculate_perplexity(candidate)
            semantic_sim = metrics.calculate_semantic_similarity(reference, candidate)
            fluency = metrics.calculate_fluency(candidate)
            coherence = metrics.calculate_coherence(candidate)
            accuracy = metrics.calculate_accuracy(reference, candidate)

            # Weighted Overall Score Formula:
            # Overall = 0.25 Accuracy + 0.20 Coherence + 0.15 Perplexity + 0.15 BLEU + 0.10 ROUGE + 0.10 Semantic Similarity + 0.05 Fluency
            overall = (0.25 * accuracy +
                       0.20 * coherence +
                       0.15 * (100 - perplexity) + # Perplexity is better when lower
                       0.15 * bleu +
                       0.10 * rouge +
                       0.10 * semantic_sim +
                       0.05 * fluency)

            results.append({
                "model": model_name,
                "accuracy": round(accuracy, 2),
                "coherence": round(coherence, 2),
                "perplexity": round(perplexity, 2),
                "bleu": round(bleu, 2),
                "rouge": round(rouge, 2),
                "semantic_similarity": round(semantic_sim, 2),
                "fluency": round(fluency, 2),
                "overall_score": round(overall, 2)
            })

        return json.dumps(results)
    except Exception as e:
        return json.dumps({"error": str(e)})

def get_sample_prompts():
    return json.dumps([
        "Explain quantum computing to a 5-year old.",
        "Write a poem about the sunset in the mountains.",
        "What are the main advantages of Kotlin over Java?",
        "How to implement a binary search tree in Python?"
    ])
