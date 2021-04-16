import json
from orderedset import OrderedSet


def prepare_data():
    with open("g1.json") as json_file:
        grammar = json.load(json_file)
        return grammar


def write_answer(grammar):
    with open("answer.json", "w", encoding="utf-8") as file:
        json.dump(grammar, file, indent=4)


def find_production(productions, name):
    for production in productions:
        if production["lhs"]["-name"] == name:
            return production


def eliminate_left_recursion(grammar):
    nonterms = grammar["grammar"]["nonterminalsymbols"]["nonterm"]
    productions = grammar["grammar"]["productions"]["production"]
    for i in range(len(nonterms)):
        ai = find_production(productions, nonterms[i]["-name"])
        for j in range(i):
            aj = find_production(productions, nonterms[j]["-name"])
            for ai_production in ai["rhs"]:
                if ai_production[0]["-name"] == aj["lhs"]["-name"]:
                    for aj_production in aj["rhs"]:
                        new_production = aj_production.copy()
                        new_production[len(new_production):len(ai_production)] = ai_production[1:]
                        ai["rhs"].append(new_production)
                    ai["rhs"].remove(ai_production)
        alpha = []
        beta = []
        for ai_production in ai["rhs"]:
            if ai_production[0]["-name"] == nonterms[i]["-name"]:
                alpha.append(ai_production[1:])
            else:
                beta.append(ai_production)
        if len(alpha) == 0:
            continue
        rhs = []
        for b in beta:
            b.append({'-type': 'nonterm', '-name': nonterms[i]["-name"] + str(1)})
            rhs.append(b)
        ai["rhs"] = rhs
        rhs = []
        for a in alpha:
            a.append({'-type': 'nonterm', '-name': nonterms[i]["-name"] + str(1)})
            rhs.append(a)
        rhs.append([{'-type': 'term', '-name': "eps"}])
        productions.append({'lhs': {'name': nonterms[i]["-name"] + str(1)}, 'rhs': rhs})
        print('')
    print('')


def eliminate_chain_rules(grammar):
    nonterms = grammar["grammar"]["nonterminalsymbols"]["nonterm"]
    productions = grammar["grammar"]["productions"]["production"]
    n = []
    for i in range(len(nonterms)):
        na = OrderedSet()
        na.add(nonterms[i]["-name"])
        j = 0
        while j < len(na):
            a = find_production(productions, na[j])
            for a_production in a["rhs"]:
                if len(a_production) == 1 and a_production[0]["-type"] == "nonterm":
                    na.add(a_production[0]["-name"])
            j = j + 1
        n.append(na)

    for i in range(len(nonterms)):
        a = find_production(productions, nonterms[i]["-name"])
        rhs = []
        for production in productions:
            if production["lhs"]["-name"] in n[i]:
                for p in production["rhs"]:
                    if not(len(p) == 1 and p[0]["-type"] == "nonterm"):
                        rhs.append(p)
        a["rhs"] = rhs


def main():
    grammar = prepare_data()
    #  eliminate_chain_rules(grammar)
    eliminate_left_recursion(grammar)
    write_answer(grammar)


if __name__ == "__main__":
    main()
